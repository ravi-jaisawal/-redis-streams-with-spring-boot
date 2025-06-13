package org.example.dto;

public class TradeMessage {
    private String tradeId;
    private String action;
    private double amount;
    private int retryCount = 0;

    public void incrementRetry() { this.retryCount++; }

    // Getters, Setters, toString
    public String getTradeId() {
        return tradeId;
    }

    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public String toString() {
        return "TradeMessage{" +
                "tradeId='" + tradeId + '\'' +
                ", action='" + action + '\'' +
                ", amount=" + amount +
                ", retryCount=" + retryCount +
                '}';
    }
}
