package org.example.controller;

import org.example.dto.TradeMessage;
import org.example.publisher.multi.RedisStreamMultiPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trade")
public class TradeController {

    @Autowired
    private RedisStreamMultiPublisher publisher;

    @PostMapping("/publish")
    public String publish(@RequestBody TradeMessage message) {
        publisher.publishToStream(message);
        return "Message published!";
    }
}
