package org.example.consumer.multi;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.constants.RedisConstants;
import org.example.dto.TradeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeProcessorB {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        try {
            redisTemplate.opsForStream().createGroup(RedisConstants.STREAM_KEY, RedisConstants.GROUP_B);
        } catch (Exception ignored) {}
    }

    @Scheduled(fixedDelay = 7000)
    public void poll() {
        List<MapRecord<String, Object, Object>> records = redisTemplate.opsForStream().read(
                Consumer.from(RedisConstants.GROUP_B, "consumer-2"),
                StreamReadOptions.empty().count(10),
                StreamOffset.create(RedisConstants.STREAM_KEY, ReadOffset.lastConsumed())
        );

        for (MapRecord<String, Object, Object> record : records) {
            try {
                TradeMessage trade = objectMapper.readValue(record.getValue().get("data").toString(), TradeMessage.class);
                System.out.println("B 🧾 AUDIT: " + trade);
                redisTemplate.opsForStream().acknowledge(RedisConstants.STREAM_KEY, RedisConstants.GROUP_B, record.getId());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
