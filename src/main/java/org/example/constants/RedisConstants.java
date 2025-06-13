package org.example.constants;

public class RedisConstants {
    public static final String STREAM_KEY = "trade-stream";
    public static final String GROUP_A = "group-a"; // e.g., order-processor
    public static final String GROUP_B = "group-b"; // e.g., audit-logger
    public static final String DLQ_KEY = "trade-dlq-stream";
    public static final int MAX_RETRIES = 3;
}