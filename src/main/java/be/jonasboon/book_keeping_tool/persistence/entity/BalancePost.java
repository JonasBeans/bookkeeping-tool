package be.jonasboon.book_keeping_tool.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "balance_posts")
public class BalancePost {

    @Id
    private String title;

    @OneToMany(mappedBy = "balancePost", cascade = CascadeType.ALL)
    private List<BalanceSubPost> balanceSubPosts;

}
