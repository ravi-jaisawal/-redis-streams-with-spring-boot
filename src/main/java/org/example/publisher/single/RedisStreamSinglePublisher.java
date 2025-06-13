package org.example.publisher.single;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

import static org.example.constants.RedisStreamConstants.STREAM_KEY;

@Service
public class RedisStreamSinglePublisher {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void publishTrade(String tradeId, String action) {
        StreamOperations<String, Object, Object> ops = stringRedisTemplate.opsForStream();
        Map<String, String> message = Map.of(
                "tradeId", tradeId,
                "action", action,
                "timestamp", String.valueOf(System.currentTimeMillis())
        );

        ops.add(STREAM_KEY, message);
        System.out.println("Published: " + message);
    }
}
