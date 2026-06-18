package es.mybi.demo.sbsender.service;

import es.mybi.demo.core.servicebus.sender.ServiceBusSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledSenderService {

    private static final Logger log = LoggerFactory.getLogger(ScheduledSenderService.class);

    @Autowired(required = false)
    private ServiceBusSender sender;

    @Value("${sender.queue:}")
    private String queueName;

    @Value("${sender.topic:}")
    private String topicName;

    @Scheduled(fixedRate = 5000)
    public void sendMessage() {
        if (sender == null) {
            log.warn("ServiceBusSender not available — check azure.servicebus.connection-string");
            return;
        }

        String message = "{\"hola\": \"mundo\"}";

        if (!queueName.isBlank()) {
            log.info("Sending to queue '{}': {}", queueName, message);
            sender.sendToQueue(queueName, message);
        } else if (!topicName.isBlank()) {
            log.info("Sending to topic '{}': {}", topicName, message);
            sender.sendToTopic(topicName, message);
        } else {
            log.warn("Neither sender.queue nor sender.topic configured — nothing to send");
        }
    }
}
