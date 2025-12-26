package com.smartinventory.transactions.service;

import com.smartinventory.transactions.model.TransactionRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionSyncService {

    private final RestTemplate restTemplate;

    public TransactionSyncService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void sendToReporting(TransactionRecord record) {
        try {
            restTemplate.postForObject(
                "http://localhost:8080/reports/transactions",
                record,
                Void.class
            );
        } catch (Exception e) {
            throw new RuntimeException("Не удалось отправить транзакцию в reporting", e);
        }
    }
}
