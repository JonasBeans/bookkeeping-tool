package be.jonasboon.book_keeping_tool.domain.balance_posts.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@Table(name = "balance_sub_posts")
@AllArgsConstructor
@NoArgsConstructor
public class BalanceSubPost {

    @Id
    private String title;
    @Setter
    private Double amount;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "balance_post_id")
    private BalancePost balancePost;
}
