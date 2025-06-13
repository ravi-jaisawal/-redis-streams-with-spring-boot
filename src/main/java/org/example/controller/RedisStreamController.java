package org.example.controller;

import org.example.publisher.single.RedisStreamSinglePublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stream")
public class RedisStreamController {

    @Autowired
    private RedisStreamSinglePublisher publisher;

    @PostMapping("/publish")
    public String publish(@RequestParam String tradeId, @RequestParam String action) {
        publisher.publishTrade(tradeId, action);
        return "Trade Event Published!";
    }
}
