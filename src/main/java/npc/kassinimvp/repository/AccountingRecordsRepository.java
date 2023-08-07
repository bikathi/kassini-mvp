package npc.kassinimvp.repository;

import npc.kassinimvp.entity.AccountingRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountingRecordsRepository extends MongoRepository<AccountingRecord, String> {
}
