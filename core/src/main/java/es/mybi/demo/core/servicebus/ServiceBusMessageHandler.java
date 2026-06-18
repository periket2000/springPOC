package es.mybi.demo.core.servicebus;

public interface ServiceBusMessageHandler {
    void handleMessage(String message);
}
