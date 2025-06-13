package org.example.consumer.single;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.constants.RedisStreamConstants.*;

@Service
public class RedisStreamConsumer {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    public void init() {
        try {
            redisTemplate.opsForStream().createGroup(STREAM_KEY, ReadOffset.latest(), GROUP_NAME);
            System.out.println("Consumer Group Created");
        } catch (Exception e) {
            System.out.println("Group may already exist: " + e.getMessage());
        }
    }

    @Scheduled(fixedDelay = 3000)
    public void pollStream() {
        List<MapRecord<String, Object, Object>> messages =
                redisTemplate.opsForStream().read(
                        Consumer.from(GROUP_NAME, CONSUMER_NAME),
                        StreamReadOptions.empty().count(10),
                        StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed())
                );

        for (MapRecord<String, Object, Object> message : messages) {
            System.out.println("Consumed: " + message.getValue());
            redisTemplate.opsForStream().acknowledge(STREAM_KEY, GROUP_NAME, message.getId());
        }
    }
}
