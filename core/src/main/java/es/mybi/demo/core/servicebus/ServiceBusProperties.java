package es.mybi.demo.core.servicebus;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "azure.servicebus")
public class ServiceBusProperties {

    private String connectionString;

    private String queueName;
    private String topicName;
    private String subscriptionName;

    private Map<String, QueueEntry> queues = new LinkedHashMap<>();
    private Map<String, TopicEntry> topics = new LinkedHashMap<>();

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getSubscriptionName() {
        return subscriptionName;
    }

    public void setSubscriptionName(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    public Map<String, QueueEntry> getQueues() {
        return queues;
    }

    public void setQueues(Map<String, QueueEntry> queues) {
        this.queues = queues;
    }

    public Map<String, TopicEntry> getTopics() {
        return topics;
    }

    public void setTopics(Map<String, TopicEntry> topics) {
        this.topics = topics;
    }

    public List<Destination> resolveDestinations() {
        List<Destination> dests = new ArrayList<>();

        if (queueName != null && !queueName.isBlank()) {
            dests.add(new Destination(null, queueName, null, null));
        }
        if (topicName != null && !topicName.isBlank()) {
            dests.add(new Destination(null, null, topicName, subscriptionName));
        }
        for (Map.Entry<String, QueueEntry> entry : queues.entrySet()) {
            String name = entry.getValue().getName();
            if (name != null && !name.isBlank()) {
                dests.add(new Destination(entry.getKey(), name, null, null));
            }
        }
        for (Map.Entry<String, TopicEntry> entry : topics.entrySet()) {
            String name = entry.getValue().getName();
            if (name != null && !name.isBlank()) {
                dests.add(new Destination(entry.getKey(), null, name, entry.getValue().getSubscription()));
            }
        }
        return dests;
    }

    public static class QueueEntry {
        private String name;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class TopicEntry {
        private String name;
        private String subscription;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getSubscription() { return subscription; }
        public void setSubscription(String subscription) { this.subscription = subscription; }
    }

    public static class Destination {
        private final String id;
        private final String queue;
        private final String topic;
        private final String subscription;

        Destination(String id, String queue, String topic, String subscription) {
            this.id = id;
            this.queue = queue;
            this.topic = topic;
            this.subscription = subscription;
        }

        public String getId() { return id; }
        public String getQueue() { return queue; }
        public String getTopic() { return topic; }
        public String getSubscription() { return subscription; }

        public boolean isQueue() { return queue != null && !queue.isBlank(); }
        public boolean isTopic() { return topic != null && !topic.isBlank(); }

        public String name() {
            if (isQueue()) return queue;
            return topic;
        }

        public String label() {
            if (isQueue()) return "queue '" + queue + "'";
            return "topic '" + topic + "' / subscription '" + subscription + "'";
        }
    }
}
