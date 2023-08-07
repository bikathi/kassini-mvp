package npc.kassinimvp.service;

import npc.kassinimvp.entity.AccountingRecord;
import npc.kassinimvp.repository.AccountingRecordsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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

    public void updateTransactionAmount(Integer newAmount, String transactionId) {
        Query query = new Query(Criteria.where("accountingId").is(transactionId));
        Update updateDefinition = new Update().set("transactionAmount", newAmount);

        FindAndModifyOptions updateOptions = new FindAndModifyOptions().returnNew(false).upsert(false);
        mongoTemplate.findAndModify(query, updateDefinition, updateOptions, AccountingRecord.class);

    }
}
