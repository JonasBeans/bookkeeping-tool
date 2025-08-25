package be.jonasboon.book_keeping_tool.persistence.changelogs;

import be.jonasboon.book_keeping_tool.persistence.entity.BalancePostEntity;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@ChangeUnit(id = "add_starter_balance_posts", order = "1", author = "Jonas Boon")
public class AddStarterBalancePosts {

    private final MongoTemplate mongoTemplate;

    @Execution
    public void changeSet() {
        mongoTemplate.insertAll(
                List.of(
                        BalancePostEntity.builder().title("Vaste activa").subPosts(new ArrayList<>()).build(),
                        BalancePostEntity.builder().title("Vlottende activa").subPosts(new ArrayList<>()).build(),
                        BalancePostEntity.builder().title("Liquide middelen").subPosts(new ArrayList<>()).build(),

                        BalancePostEntity.builder().title("Eigen vermogen").subPosts(new ArrayList<>()).build(),
                        BalancePostEntity.builder().title("Langlopende schulden").subPosts(new ArrayList<>()).build(),
                        BalancePostEntity.builder().title("Kortlopende schulden").subPosts(new ArrayList<>()).build()
                )
        );
    }

    @RollbackExecution
    public void rollback() {
    }

}
