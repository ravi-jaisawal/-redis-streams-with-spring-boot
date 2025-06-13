package org.example.consumer.multi;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.example.constants.RedisConstants;
import org.example.dto.TradeMessage;
import org.example.publisher.multi.RedisStreamMultiPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TradeProcessorA {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private RedisStreamMultiPublisher publisher;

    @PostConstruct
    public void init() {
        try {
            redisTemplate.opsForStream().createGroup(RedisConstants.STREAM_KEY, RedisConstants.GROUP_A);
        } catch (Exception ignored) {}
    }

    @Scheduled(fixedDelay = 5000)
    public void poll() {
        List<MapRecord<String, Object, Object>> records = redisTemplate.opsForStream().read(
                Consumer.from(RedisConstants.GROUP_A, "consumer-1"),
                StreamReadOptions.empty().count(10),
                StreamOffset.create(RedisConstants.STREAM_KEY, ReadOffset.lastConsumed())
        );

        for (MapRecord<String, Object, Object> record : records) {
            try {
                TradeMessage trade = objectMapper.readValue(record.getValue().get("data").toString(), TradeMessage.class);

                // simulate error
                if (trade.getTradeId().endsWith("FAIL")) throw new RuntimeException("Processing failed");

                System.out.println("A ✅: " + trade);
                redisTemplate.opsForStream().acknowledge(RedisConstants.STREAM_KEY, RedisConstants.GROUP_A, record.getId());

            } catch (Exception ex) {
                try {
                    TradeMessage trade = objectMapper.readValue(record.getValue().get("data").toString(), TradeMessage.class);
                    trade.incrementRetry();
                    if (trade.getRetryCount() > RedisConstants.MAX_RETRIES) {
                        publisher.sendToDLQ(trade);
                        System.out.println("A ☠️ DLQ: " + trade);
                        redisTemplate.opsForStream().acknowledge(RedisConstants.STREAM_KEY, RedisConstants.GROUP_A, record.getId());
                    } else {
                        redisTemplate.opsForStream().add(RedisConstants.STREAM_KEY, Map.of("data", objectMapper.writeValueAsString(trade)));
                        redisTemplate.opsForStream().acknowledge(RedisConstants.STREAM_KEY, RedisConstants.GROUP_A, record.getId());
                        System.out.println("A 🔁 RETRY: " + trade);
                    }
                } catch (Exception innerEx) {
                    innerEx.printStackTrace();
                }
            }
        }
    }
}
