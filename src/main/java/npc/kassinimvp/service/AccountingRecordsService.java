package npc.kassinimvp.service;

import npc.kassinimvp.entity.AccountingRecord;
import npc.kassinimvp.repository.AccountingRecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountingRecordsService {
    @Autowired
    AccountingRecordsRepository accountingRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    public void saveNewTransaction(AccountingRecord newRecord) {
        accountingRepository.save(newRecord);
    }

    public List<AccountingRecord> findRecordsByQuery(String userId, Integer year) {
        Query query = new Query(Criteria.where("recordCreatorId").is(userId).and("yearOfTransaction").is(year));
        return mongoTemplate.find(query, AccountingRecord.class, "accounting-records");
    }
}
