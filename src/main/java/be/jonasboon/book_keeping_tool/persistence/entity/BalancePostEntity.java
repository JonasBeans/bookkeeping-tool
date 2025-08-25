package be.jonasboon.book_keeping_tool.persistence.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Builder
@Document(collection = "balance_posts")
public class BalancePostEntity {

    @Id
    String id;
    String title;
    List<SubPostEntity> subPosts;

    @Getter
    @Setter
    @Builder
    public static class SubPostEntity {
        private String title;
        @Setter
        private Double amount;
    }

}
