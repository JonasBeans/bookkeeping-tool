package be.jonasboon.book_keeping_tool.domain.balance_posts.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "balance_posts")
public class BalancePost implements Serializable {

    @Id
    private String title;

    @JsonIgnore
    @OneToMany(mappedBy = "balancePost", cascade = CascadeType.ALL)
    private List<BalanceSubPost> balanceSubPosts;

}
