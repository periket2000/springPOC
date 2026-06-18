package es.mybi.demo.sbpoc.service;

import es.mybi.demo.core.servicebus.receiver.ServiceBusMessageHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PrintMessageHandler implements ServiceBusMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(PrintMessageHandler.class);

    @Override
    public void handleMessage(String message) {
        System.out.println("========================================");
        System.out.println("  SERVICE BUS MESSAGE");
        System.out.println("========================================");
        System.out.println("  " + message);
        System.out.println("========================================");
        log.info("Message processed: {}", message);
    }
}
