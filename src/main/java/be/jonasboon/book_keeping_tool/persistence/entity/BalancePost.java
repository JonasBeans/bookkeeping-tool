package be.jonasboon.book_keeping_tool.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
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

    @OneToMany(mappedBy = "title")
    private List<BalanceSubPost> balanceSubPosts;

}
