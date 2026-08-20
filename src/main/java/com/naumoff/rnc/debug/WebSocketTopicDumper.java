package com.naumoff.rnc.debug;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketTopicDumper implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        System.out.println(message);
        // Log destination topic and payload
        if (accessor.getDestination() != null) {
            String topic = accessor.getDestination();
            String payload = new String((byte[]) message.getPayload());
            System.out.println(message.getPayload());
            
            System.out.println("Topic: " + topic + " | Payload: " + payload);
            
            // TODO: Dump to a file, database, or Kafka stream here
        }
        
        return message;
    }
}