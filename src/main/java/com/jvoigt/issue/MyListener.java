package com.jvoigt.issue;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.MessageListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyListener implements MessageListener {

    @Override
    public void received(Consumer consumer, Message msg) {
        // I dont care about the types
        log.info(" {} => Message received: {}", msg.getTopicName(), new String(msg.getData()));

        log.error("{} => I WILL NACK IT with Counter: {}",msg.getTopicName(), msg.getRedeliveryCount());
        consumer.negativeAcknowledge(msg);
    }
}
