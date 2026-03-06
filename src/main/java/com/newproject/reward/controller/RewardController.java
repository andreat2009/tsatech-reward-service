package com.newproject.reward.controller;

import com.newproject.reward.dto.RewardSummaryResponse;
import com.newproject.reward.dto.RewardTransactionRequest;
import com.newproject.reward.dto.RewardTransactionResponse;
import com.newproject.reward.service.RewardService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers/{customerId}/rewards")
public class RewardController {
    private final RewardService rewardService;

    public RewardController(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    @GetMapping("/summary")
    public RewardSummaryResponse getSummary(@PathVariable Long customerId) {
        return rewardService.getSummary(customerId);
    }

    @GetMapping("/transactions")
    public List<RewardTransactionResponse> listTransactions(@PathVariable Long customerId) {
        return rewardService.listTransactions(customerId);
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public RewardTransactionResponse addTransaction(
        @PathVariable Long customerId,
        @Valid @RequestBody RewardTransactionRequest request
    ) {
        return rewardService.addTransaction(customerId, request);
    }
}
