package com.smartinventory.transactions.controller;

import com.smartinventory.transactions.dto.TransactionRequest;
import com.smartinventory.transactions.model.TransactionRecord;
import com.smartinventory.transactions.repository.TransactionRepository;
import com.smartinventory.transactions.service.TransactionSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TransactionSyncService transactionSyncService;


    public TransactionController(TransactionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TransactionRecord> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionRecord> getById(@PathVariable Long id) {
        Optional<TransactionRecord> opt = repository.findById(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TransactionRecord> create(@RequestBody TransactionRequest request) {
        try {

            if (request.getProductId() == null || request.getType() == null || request.getQuantity() <= 0) {
                return ResponseEntity.badRequest().build();
            }

            String type = request.getType().trim().toUpperCase();
            if (!type.equals("IN") && !type.equals("OUT")) {
                return ResponseEntity.badRequest().build();
            }

            if (type.equals("OUT")) {

                restTemplate.postForObject(
                        "http://localhost:8080/inventory/" + request.getProductId() + "/decrease?qty=" + request.getQuantity(),
                        null,
                        Void.class
                );
            } else {
                restTemplate.postForObject(
                        "http://localhost:8080/inventory/" + request.getProductId() + "/increase?qty=" + request.getProductId(),
                        null,
                        Void.class
                );
            }

            TransactionRecord record = new TransactionRecord(request.getProductId(), type, request.getQuantity());
            TransactionRecord saved = repository.save(record);
            transactionSyncService.sendToReporting(saved);
            return ResponseEntity.ok(saved);
        }catch (HttpClientErrorException.BadRequest e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
