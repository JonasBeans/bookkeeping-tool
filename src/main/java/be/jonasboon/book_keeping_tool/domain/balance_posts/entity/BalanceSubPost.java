package be.jonasboon.book_keeping_tool.domain.balance_posts.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@Entity
@Table(name = "balance_sub_posts")
@AllArgsConstructor
@NoArgsConstructor
public class BalanceSubPost implements Serializable {

    @Id
    private String title;
    @Setter
    private Double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "balance_post_id")
    private BalancePost balancePost;
}
