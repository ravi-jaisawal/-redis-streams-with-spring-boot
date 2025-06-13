# Project Title

redis-streams-with-spring-boot



```http
curl -X POST http://localhost:8080/trade/publish \
  -H "Content-Type: application/json" \
  -d '{"tradeId":"TX100", "action":"BUY", "amount":1000}'
```


```http
curl -X POST http://localhost:8080/trade/publish \
  -H "Content-Type: application/json" \
  -d '{"tradeId":"TX_FAIL", "action":"SELL", "amount":1000}'
```
