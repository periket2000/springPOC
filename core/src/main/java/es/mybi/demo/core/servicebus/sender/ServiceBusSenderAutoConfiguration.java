package es.mybi.demo.core.servicebus.sender;

import es.mybi.demo.core.servicebus.ServiceBusProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("azure.servicebus.connection-string")
public class ServiceBusSenderAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ServiceBusSenderAutoConfiguration.class);

    @Bean
    public ServiceBusSender serviceBusSender(ServiceBusProperties properties) {
        log.info("ServiceBus sender auto-configuration activated");
        return new ServiceBusSender(properties.getConnectionString());
    }
}
