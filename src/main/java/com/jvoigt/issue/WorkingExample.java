package com.jvoigt.issue;

import java.io.Reader;
import java.util.concurrent.TimeUnit;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.DeadLetterPolicy;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionType;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WorkingExample {

    public static void main(String[] args) throws PulsarClientException {
        log.info("Starting Client....");
        String pulsarUrl = "pulsar://localhost:6650";
        PulsarClient pulsarClient = PulsarClient.builder().serviceUrl(pulsarUrl).build();

        Producer<String> stringProducer = pulsarClient.newProducer(Schema.STRING).topic("topic").create();
        stringProducer.send("My message");
        stringProducer.flush();
        stringProducer.close();

        Consumer pulsarConsumer = pulsarClient.newConsumer(Schema.STRING).topic("topic")
                .subscriptionType(SubscriptionType.Shared).subscriptionName("subscription")
                .ackTimeout(2, TimeUnit.SECONDS)
                .deadLetterPolicy(DeadLetterPolicy.builder().maxRedeliverCount(3).build())
                .messageListener(new MyListener()).subscribe();
                
        Consumer deadConsumer = pulsarClient.newConsumer(Schema.STRING).topic("topic-subscription-DLQ")
                .subscriptionType(SubscriptionType.Shared).subscriptionName("dead")
                .messageListener(new MyListener()).subscribe();
    }
}
