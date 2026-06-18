package es.mybi.demo.core.servicebus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@EnableConfigurationProperties(ServiceBusProperties.class)
@ConditionalOnProperty("azure.servicebus.connection-string")
public class ServiceBusAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ServiceBusAutoConfiguration.class);

    @Bean(initMethod = "start", destroyMethod = "stop")
    public ServiceBusConsumer serviceBusConsumer(
            ServiceBusProperties properties,
            Optional<ServiceBusMessageHandler> messageHandler) {
        log.info("ServiceBus auto-configuration activated — creating consumer");
        if (messageHandler.isPresent()) {
            log.info("Message handler found: {}", messageHandler.get().getClass().getSimpleName());
        } else {
            log.warn("No ServiceBusMessageHandler bean found — messages will be logged but not processed");
        }
        return new ServiceBusConsumer(properties, messageHandler);
    }
}
