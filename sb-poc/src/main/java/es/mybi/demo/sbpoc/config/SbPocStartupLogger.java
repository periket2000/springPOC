package es.mybi.demo.sbpoc.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SbPocStartupLogger implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SbPocStartupLogger.class);

    @Value("${azure.servicebus.connection-string:}")
    private String connectionString;

    @Value("${azure.servicebus.queue-name:}")
    private String queueName;

    @Value("${azure.servicebus.topic-name:}")
    private String topicName;

    @Value("${azure.servicebus.subscription-name:}")
    private String subscriptionName;

    @Override
    public void run(String... args) {
        log.info("========================================");
        log.info("  sb-poc application started");
        log.info("========================================");

        if (!connectionString.isBlank()) {
            log.info("ServiceBus consumer is ACTIVE");
            if (!queueName.isBlank()) {
                log.info("  Listening to queue: {}", queueName);
            } else if (!topicName.isBlank() && !subscriptionName.isBlank()) {
                log.info("  Listening to topic: {} / subscription: {}", topicName, subscriptionName);
            }
        } else {
            log.warn("ServiceBus consumer is DISABLED (azure.servicebus.connection-string not set)");
        }
    }
}
