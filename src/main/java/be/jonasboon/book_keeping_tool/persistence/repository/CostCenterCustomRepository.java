package be.jonasboon.book_keeping_tool.persistence.repository;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
public class CostCenterCustomRepository {

    private final MongoTemplate mongoTemplate;

    public CostCenterCustomRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void resetTotalAllAmounts() {
        mongoTemplate.updateMulti(
                new Query(),
                new Update().set("totalAmount", 0.0),
                "cost_centers"
        );
    }

}
