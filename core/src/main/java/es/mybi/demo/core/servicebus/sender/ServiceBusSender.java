package es.mybi.demo.core.servicebus.sender;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusSenderAsyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceBusSender {

    private static final Logger log = LoggerFactory.getLogger(ServiceBusSender.class);

    private final String connectionString;

    public ServiceBusSender(String connectionString) {
        this.connectionString = connectionString;
    }

    public void sendToQueue(String queueName, String message) {
        ServiceBusSenderAsyncClient sender = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .queueName(queueName)
                .buildAsyncClient();

        sender.sendMessage(new com.azure.messaging.servicebus.ServiceBusMessage(message))
                .doOnSuccess(v -> log.info("Message sent to queue '{}': {}", queueName, message))
                .doOnError(e -> log.error("Failed to send message to queue '{}'", queueName, e))
                .block();
    }

    public void sendToTopic(String topicName, String message) {
        ServiceBusSenderAsyncClient sender = new ServiceBusClientBuilder()
                .connectionString(connectionString)
                .sender()
                .topicName(topicName)
                .buildAsyncClient();

        sender.sendMessage(new com.azure.messaging.servicebus.ServiceBusMessage(message))
                .doOnSuccess(v -> log.info("Message sent to topic '{}': {}", topicName, message))
                .doOnError(e -> log.error("Failed to send message to topic '{}'", topicName, e))
                .block();
    }
}
