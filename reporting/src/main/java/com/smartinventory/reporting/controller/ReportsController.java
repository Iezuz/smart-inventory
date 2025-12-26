package com.smartinventory.reporting.controller;

import com.smartinventory.reporting.model.TransactionRecord;
import com.smartinventory.reporting.repository.TransactionRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/reports")
public class ReportsController {

    private final TransactionRepository repository;

    public ReportsController(TransactionRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/movement")
    public List<TransactionRecord> movement(
            @RequestParam(name = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        LocalDateTime fromDt = (from != null) ? from.atStartOfDay() : LocalDateTime.MIN;
        LocalDateTime toDt = (to != null) ? to.atTime(LocalTime.MAX) : LocalDateTime.now();
        return repository.findByCreatedAtBetween(fromDt, toDt);
    }

    @GetMapping("/stock-summary")
    public List<Map<String, Object>> stockSummary() {
        List<Object[]> raw = repository.calculateBalanceByProduct();
        return raw.stream().map(r -> Map.of("productId", r[0], "balance", ((Number)r[1]).intValue())).collect(Collectors.toList());
    }

    @GetMapping("/stock-low")
    public ResponseEntity<List<Map<String, Object>>> stockLow(@RequestParam(name = "threshold", defaultValue = "5") int threshold) {
        List<Map<String, Object>> summary = stockSummary();
        List<Map<String, Object>> low = summary.stream()
                .filter(m -> ((Integer)m.get("balance")) < threshold)
                .collect(Collectors.toList());
        return ResponseEntity.ok(low);
    }

    @PostMapping("/transactions")
    public ResponseEntity<Void> receiveTransaction(@RequestBody TransactionRecord record) {
        if (repository.existsById(record.getId())) {
            return ResponseEntity.ok().build();
        }
        repository.save(record);
        return ResponseEntity.ok().build();
    }
}
