package com.jvoigt.issue;

import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.DeadLetterPolicy;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.client.impl.schema.JSONSchema;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BrokenJsonExample {

    public static void main(String[] args) throws PulsarClientException {
        log.info("Starting Client....");
        String pulsarUrl = "pulsar://localhost:6650";
        PulsarClient pulsarClient = PulsarClient.builder().serviceUrl(pulsarUrl).build();
        
        Schema<Payload> schema = JSONSchema.of(Payload.class); 
        String topic = "jsonTopic";
        
        log.info("Starting Producer....");
        Payload payload = new Payload();
        payload.message = "My Payload";
        Producer<Payload> stringProducer = pulsarClient.newProducer(schema).topic(topic).create();
        stringProducer.send(payload);
        stringProducer.flush();
        stringProducer.close();
        
        log.info("Starting Consumer....");
        Consumer<Payload> pulsarConsumer = pulsarClient.newConsumer(schema).topic(topic)
        .subscriptionType(SubscriptionType.Shared).subscriptionName("subscription")
        .ackTimeout(2, TimeUnit.SECONDS)
        .deadLetterPolicy(DeadLetterPolicy.builder().maxRedeliverCount(3).build())
        .messageListener(new MyListener()).subscribe();
        
        log.info("Starting DLQ-Consumer....");
        Consumer<Payload> deadConsumer = pulsarClient.newConsumer(schema).topic("schema-jsonTopic-subscription-DLQ")
                .subscriptionType(SubscriptionType.Shared).subscriptionName("dead")
                .messageListener(new MyListener()).subscribe();
    }
}
