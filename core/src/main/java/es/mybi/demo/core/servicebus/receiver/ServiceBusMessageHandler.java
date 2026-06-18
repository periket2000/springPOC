package es.mybi.demo.core.servicebus.receiver;

public interface ServiceBusMessageHandler {
    void handleMessage(String message);
}
