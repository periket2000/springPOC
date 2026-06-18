package es.mybi.demo.core.servicebus.receiver;

import es.mybi.demo.core.servicebus.ServiceBusProperties;
import es.mybi.demo.core.servicebus.ServiceBusProperties.Destination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(ServiceBusProperties.class)
@ConditionalOnProperty("azure.servicebus.connection-string")
public class ServiceBusAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ServiceBusAutoConfiguration.class);

    @Bean(initMethod = "start", destroyMethod = "stop")
    public ServiceBusConsumer serviceBusConsumer(
            ServiceBusProperties properties,
            BeanFactory beanFactory) {

        List<Destination> destinations = properties.resolveDestinations();
        Map<String, ServiceBusMessageHandler> handlerMap = new HashMap<>();

        for (Destination dest : destinations) {
            String id = dest.getId() != null ? dest.getId() : dest.name();
            String beanName = sanitizeBeanName(id) + "Handler";

            ServiceBusMessageHandler handler = null;

            try {
                handler = BeanFactoryAnnotationUtils.qualifiedBeanOfType(
                        beanFactory, ServiceBusMessageHandler.class, beanName);
                log.info("Destination key '{}' -> bean '{}' (class {})", id, beanName,
                    handler.getClass().getSimpleName());
            } catch (NoSuchBeanDefinitionException e) {
                log.info("No bean '{}' found for key '{}' — using default handler", beanName, id);
            }

            if (handler == null) {
                handler = new DefaultMessageHandler();
            }

            handlerMap.put(id, handler);
        }

        log.info("ServiceBus auto-configuration activated — {} destination(s) mapped", handlerMap.size());
        return new ServiceBusConsumer(properties, handlerMap);
    }

    private static String sanitizeBeanName(String id) {
        String cleaned = id.replaceAll("[^a-zA-Z0-9]", "");
        if (cleaned.isEmpty()) {
            return "default";
        }
        if (!Character.isLetter(cleaned.charAt(0))) {
            return "q" + cleaned;
        }
        return cleaned;
    }
}
