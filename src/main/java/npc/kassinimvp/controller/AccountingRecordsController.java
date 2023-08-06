package npc.kassinimvp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api/v1/transactions")
public class AccountingRecordsController {
    @PostMapping(value = "/new-transaction")
    public ResponseEntity<?> recordNewTransaction() {
        return null;
    }

    @PostMapping(value = "/anon-transaction")
    public ResponseEntity<?> recordAnonymousTransaction() {
        return null;
    }

    @GetMapping(value = "/get-transactions")
    public ResponseEntity<?> getTransactionHistory() {
        return null;
    }

    @PatchMapping(value = "/update-amount")
    public ResponseEntity<?> updateTransactionAmount() {
        return null;
    }
}
