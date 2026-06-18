package es.mybi.demo.core.servicebus.receiver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultMessageHandler implements ServiceBusMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultMessageHandler.class);

    @Override
    public void handleMessage(String message) {
        System.out.println("========================================");
        System.out.println("  [DEFAULT HANDLER] ServiceBus Message");
        System.out.println("========================================");
        System.out.println("  " + message);
        System.out.println("========================================");
        log.info("Default handler processed message: {}", message);
    }
}
