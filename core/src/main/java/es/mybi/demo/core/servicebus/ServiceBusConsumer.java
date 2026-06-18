package es.mybi.demo.core.servicebus;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusReceiverAsyncClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class ServiceBusConsumer {

    private static final Logger log = LoggerFactory.getLogger(ServiceBusConsumer.class);

    private final ServiceBusProperties properties;
    private final Optional<ServiceBusMessageHandler> messageHandler;
    private ServiceBusReceiverAsyncClient receiverClient;

    public ServiceBusConsumer(ServiceBusProperties properties, Optional<ServiceBusMessageHandler> messageHandler) {
        this.properties = properties;
        this.messageHandler = messageHandler;
    }

    public void start() {
        String connStr = properties.getConnectionString();
        String queue = properties.getQueueName();
        String topic = properties.getTopicName();
        String subscription = properties.getSubscriptionName();

        if (connStr == null || connStr.isBlank()) {
            log.warn("azure.servicebus.connection-string not set — ServiceBus consumer disabled");
            return;
        }

        ServiceBusClientBuilder builder = new ServiceBusClientBuilder()
                .connectionString(connStr);

        if (queue != null && !queue.isBlank()) {
            log.info("Listening to ServiceBus queue: {}", queue);
            receiverClient = builder.receiver()
                    .queueName(queue)
                    .receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
                    .buildAsyncClient();
        } else if (topic != null && !topic.isBlank() && subscription != null && !subscription.isBlank()) {
            log.info("Listening to ServiceBus topic: {} / subscription: {}", topic, subscription);
            receiverClient = builder.receiver()
                    .topicName(topic)
                    .subscriptionName(subscription)
                    .receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
                    .buildAsyncClient();
        } else {
            log.warn("Neither queue nor topic/subscription configured — ServiceBus consumer disabled");
            return;
        }

        receiverClient.receiveMessages()
                .subscribe(message -> {
                    String body = message.getBody().toString();
                    log.info("Received message: {}", body);
                    messageHandler.ifPresentOrElse(
                            handler -> handler.handleMessage(body),
                            () -> log.info("No message handler registered — message ignored"));
                    receiverClient.complete(message).block();
                }, error -> {
                    log.error("Error receiving message", error);
                });
    }

    @PreDestroy
    public void stop() {
        if (receiverClient != null) {
            receiverClient.close();
        }
    }
}
