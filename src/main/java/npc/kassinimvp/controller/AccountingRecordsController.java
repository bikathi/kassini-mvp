package npc.kassinimvp.controller;

import lombok.extern.slf4j.Slf4j;
import npc.kassinimvp.entity.AccountingRecord;
import npc.kassinimvp.entity.definitions.BuyerDetails;
import npc.kassinimvp.payload.request.NewAnonTransactionRequest;
import npc.kassinimvp.payload.request.RecordTransactionRequest;
import npc.kassinimvp.payload.response.GenericServiceResponse;
import npc.kassinimvp.payload.response.MessageResponse;
import npc.kassinimvp.security.service.UserDetailsImpl;
import npc.kassinimvp.service.AccountingRecordsService;
import npc.kassinimvp.service.ProductPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/api/v1/transactions")
public class AccountingRecordsController {
    @Autowired
    private AccountingRecordsService transactionsService;

    @Autowired
    private ProductPostService postService;

    @Value("${service.api.version}")
    private String apiVersion;

    @Value("${service.organization.name}")
    private String organizationName;

    @PostMapping(value = "/new-transaction")
    @PreAuthorize("hasAuthority('ROLE_VENDOR')")
    public ResponseEntity<?> recordNewTransaction(@RequestBody RecordTransactionRequest newTransactionRequest) {
        // fashion up the AccountingRecord
        String transactionId = generateTransactionID();
        AccountingRecord transactionRecord = new AccountingRecord(
            transactionId,
            userDetails().getUserId(),
            true,
            newTransactionRequest.getPostId(),
            newTransactionRequest.getDateOfTransaction(), // for example 21/01/2023
            newTransactionRequest.getTransactionAmount(),
            newTransactionRequest.getFromUserId(), // you the buyer
            newTransactionRequest.getToUserId(), // me the vendor
            newTransactionRequest.getMoneyDirection(), // money in or money out
            newTransactionRequest.getMonthOfTransaction(),
            newTransactionRequest.getYearOfTransaction(),
            new BuyerDetails(newTransactionRequest.getBuyerDetails())
        );

        // we save the record to the database and then update the status of the post to sold
        // then we send the appropriate response to the user
        try {
            transactionsService.saveNewTransaction(transactionRecord);
            log.info("Saved new transaction record id {} for vendor {}", transactionId, newTransactionRequest.getToUserId());

            // mark the postId as status true
            postService.updateProductStatus(newTransactionRequest.getPostId());
            log.info("Updated status of post {} to true", newTransactionRequest.getPostId());

            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully saved new transaction", HttpStatus.OK.value(), new MessageResponse("Successfully saved new transaction")));
        } catch(Exception ex) {
            log.error("Server experienced error saving transaction record for vendor {}", newTransactionRequest.getToUserId());
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName, "Server experienced an error saving transaction record.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new MessageResponse("Server experienced an error. Please try again")));
        }
    }

    /**
     * These are transactions outside the scope of the application
     */
    @PostMapping(value = "/anon-transaction")
    @PreAuthorize("hasAuthority('ROLE_VENDOR') or hasAuthority('ROLE_BUYER')")
    public ResponseEntity<?> recordAnonymousTransaction(@RequestBody NewAnonTransactionRequest anonTransactionRequest) {
        // fashion up the AccountingRecord
        String transactionId = generateTransactionID();
        AccountingRecord transactionRecord = new AccountingRecord(
            transactionId,
            userDetails().getUserId(),
            false,
            null,
            anonTransactionRequest.getDateOfTransaction(),
            anonTransactionRequest.getTransactionAmount(),
            anonTransactionRequest.getFromUserId(),
            anonTransactionRequest.getToUserId(),
            anonTransactionRequest.getMoneyDirection(),
            anonTransactionRequest.getMonthOfTransaction(),
            anonTransactionRequest.getYearOfTransaction(),
            null
        );

        try {
            transactionsService.saveNewTransaction(transactionRecord);
            log.info("Saved new anon transaction record id {}", transactionId);

            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully saved new anon transaction", HttpStatus.OK.value(), new MessageResponse("Successfully signed up user")));
        } catch(Exception ex) {
            log.error("Server experienced error saving anon transaction record");
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName, "Server experienced an error saving anon transaction record.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new MessageResponse("Server experienced an error. Please try again")));
        }
    }

    @GetMapping(value = "/get-transactions")
    @PreAuthorize("hasAuthority('ROLE_VENDOR') or hasAuthority('ROLE_BUYER')")
    public ResponseEntity<?> getTransactionHistory(@RequestParam String userId, @RequestParam String year) {
        // convert year to equivalent integer
        int requestedYear = Integer.parseInt(year);

        // get the list according to the record creator's ID and the year provided
        try {
            List<AccountingRecord> retrievedList = transactionsService.findRecordsByQuery(userId, requestedYear);
            if(retrievedList.isEmpty()) {
                log.info("Found empty records list for userId {}", userId);
                return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName, "Found empty records list for that user",
                    HttpStatus.OK.value(),
                    new MessageResponse("No records found for that user")));
            }

            log.info("Found records list for userId {}", userId);
            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName, "Found records list for that user",
                HttpStatus.OK.value(),
                retrievedList));
        } catch(Exception ex) {
            log.error("Server experienced error retrieving transaction record");
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName, "Server experienced error retrieving transaction record",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new MessageResponse("Server experienced an error. Please try again")));
        }
    }

    @PatchMapping(value = "/update-amount")
    @PreAuthorize("hasAuthority('ROLE_VENDOR') or hasAuthority('ROLE_BUYER')")
    public ResponseEntity<?> updateTransactionAmount(@RequestParam String transactionId, @RequestParam String newAmount) {
        int amount = Integer.parseInt(newAmount);
        try {
            transactionsService.updateTransactionAmount(amount, transactionId);

            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName, "Successfully updated transaction amount",
                HttpStatus.OK.value(), null));
        } catch(Exception ex) {
            log.error("Server experienced error updating transaction amount for transaction {}", transactionId);
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName, "Server experienced error updating transaction amount",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new MessageResponse("Server experienced an error. Please try again")));
        }
    }

    private String generateTransactionID() {
        return UUID.randomUUID().toString()
                .replace("-", "").substring(0, 15);
    }

    private UserDetailsImpl userDetails() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
