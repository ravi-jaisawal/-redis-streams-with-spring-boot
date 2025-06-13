package org.example.publisher.multi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.constants.RedisConstants;
import org.example.dto.TradeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RedisStreamMultiPublisher {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired private ObjectMapper objectMapper;

    public void publishToStream(TradeMessage trade) {
        try {
            String json = objectMapper.writeValueAsString(trade);
            redisTemplate.opsForStream().add(RedisConstants.STREAM_KEY, Map.of("data", json));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendToDLQ(TradeMessage trade) {
        try {
            String json = objectMapper.writeValueAsString(trade);
            redisTemplate.opsForStream().add(RedisConstants.DLQ_KEY, Map.of("data", json));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
