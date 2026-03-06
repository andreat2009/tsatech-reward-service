package com.newproject.reward.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RewardTransactionRequest {
    private Long orderId;

    @NotBlank
    private String description;

    @NotNull
    private Integer pointsDelta;

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getPointsDelta() { return pointsDelta; }
    public void setPointsDelta(Integer pointsDelta) { this.pointsDelta = pointsDelta; }
}
