package com.newproject.reward.service;

import com.newproject.reward.domain.RewardTransaction;
import com.newproject.reward.dto.RewardSummaryResponse;
import com.newproject.reward.dto.RewardTransactionRequest;
import com.newproject.reward.dto.RewardTransactionResponse;
import com.newproject.reward.events.EventPublisher;
import com.newproject.reward.repository.RewardTransactionRepository;
import com.newproject.reward.security.RequestActor;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RewardService {
    private final RewardTransactionRepository repository;
    private final EventPublisher eventPublisher;
    private final RequestActor requestActor;

    public RewardService(RewardTransactionRepository repository, EventPublisher eventPublisher, RequestActor requestActor) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.requestActor = requestActor;
    }

    @Transactional(readOnly = true)
    public RewardSummaryResponse getSummary(Long customerId) {
        requestActor.assertCustomerAccessIfAuthenticated(customerId);
        RewardSummaryResponse response = new RewardSummaryResponse();
        response.setCustomerId(customerId);
        response.setPointsBalance(repository.sumPointsByCustomerId(customerId));
        return response;
    }

    @Transactional(readOnly = true)
    public List<RewardTransactionResponse> listTransactions(Long customerId) {
        requestActor.assertCustomerAccessIfAuthenticated(customerId);
        return repository.findByCustomerIdOrderByCreatedAtDesc(customerId)
            .stream()
            .map(this::toResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public RewardTransactionResponse addTransaction(Long customerId, RewardTransactionRequest request) {
        // SECURITY (H1): l'accredito di punti porta valore -> solo ADMIN. Un cliente
        // autenticato sul proprio customerId poteva accreditarsi punti arbitrari (pointsDelta
        // dal body). L'earn automatico, se introdotto, andra' fatto via consumer eventi interno.
        requestActor.assertAdmin();
        RewardTransaction transaction = new RewardTransaction();
        transaction.setCustomerId(customerId);
        transaction.setOrderId(request.getOrderId());
        transaction.setDescription(request.getDescription());
        transaction.setPointsDelta(request.getPointsDelta());
        transaction.setCreatedAt(OffsetDateTime.now());

        RewardTransaction saved = repository.save(transaction);
        RewardTransactionResponse response = toResponse(saved);
        eventPublisher.publish("REWARD_TRANSACTION_CREATED", "reward_transaction", saved.getId().toString(), response);
        return response;
    }

    private RewardTransactionResponse toResponse(RewardTransaction transaction) {
        RewardTransactionResponse response = new RewardTransactionResponse();
        response.setId(transaction.getId());
        response.setCustomerId(transaction.getCustomerId());
        response.setOrderId(transaction.getOrderId());
        response.setDescription(transaction.getDescription());
        response.setPointsDelta(transaction.getPointsDelta());
        response.setCreatedAt(transaction.getCreatedAt());
        return response;
    }
}
