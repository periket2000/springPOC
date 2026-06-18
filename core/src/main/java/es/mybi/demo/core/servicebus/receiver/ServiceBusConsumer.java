package es.mybi.demo.core.servicebus.receiver;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusReceiverAsyncClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import es.mybi.demo.core.servicebus.ServiceBusProperties.Destination;
import es.mybi.demo.core.servicebus.ServiceBusProperties;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServiceBusConsumer {

    private static final Logger log = LoggerFactory.getLogger(ServiceBusConsumer.class);

    private final ServiceBusProperties properties;
    private final Map<String, ServiceBusMessageHandler> handlers;
    private final List<ServiceBusReceiverAsyncClient> receiverClients = new ArrayList<>();

    public ServiceBusConsumer(ServiceBusProperties properties, Map<String, ServiceBusMessageHandler> handlers) {
        this.properties = properties;
        this.handlers = handlers;
    }

    public void start() {
        String connStr = properties.getConnectionString();

        if (connStr == null || connStr.isBlank()) {
            log.warn("azure.servicebus.connection-string not set — ServiceBus consumer disabled");
            return;
        }

        List<Destination> destinations = properties.resolveDestinations();
        if (destinations.isEmpty()) {
            log.warn("No queues or topics configured — ServiceBus consumer disabled");
            return;
        }

        ServiceBusClientBuilder builder = new ServiceBusClientBuilder()
                .connectionString(connStr);

        for (Destination dest : destinations) {
            String destKey = dest.getId() != null ? dest.getId() : dest.name();
            ServiceBusReceiverAsyncClient client;

            if (dest.isQueue()) {
                log.info("Listening to queue: {}", dest.getQueue());
                client = builder.receiver()
                        .queueName(dest.getQueue())
                        .receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
                        .buildAsyncClient();
            } else {
                log.info("Listening to topic: {} / subscription: {}", dest.getTopic(), dest.getSubscription());
                client = builder.receiver()
                        .topicName(dest.getTopic())
                        .subscriptionName(dest.getSubscription())
                        .receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
                        .buildAsyncClient();
            }

            receiverClients.add(client);

            ServiceBusMessageHandler handler = handlers.get(destKey);
            if (handler != null) {
                log.info("Handler '{}' registered for destination key '{}'", 
                    handler.getClass().getSimpleName(), destKey);
            }

            client.receiveMessages()
                    .subscribe(message -> {
                        String body = message.getBody().toString();
                        log.info("Received message from {}: {}", dest.label(), body);
                        if (handler != null) {
                            handler.handleMessage(body);
                        } else {
                            log.warn("No handler registered for destination key '{}' — message ignored", destKey);
                        }
                        client.complete(message).block();
                    }, error -> {
                        log.error("Error receiving message from {}", dest.label(), error);
                    });
        }
    }

    @PreDestroy
    public void stop() {
        receiverClients.forEach(client -> {
            try {
                client.close();
            } catch (Exception e) {
                log.warn("Error closing receiver client", e);
            }
        });
        receiverClients.clear();
    }
}
