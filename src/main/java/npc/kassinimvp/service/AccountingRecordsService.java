package npc.kassinimvp.service;

import npc.kassinimvp.repository.AccountingRecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountingRecordsService {
    @Autowired
    AccountingRecordsRepository accountingRepository;
}