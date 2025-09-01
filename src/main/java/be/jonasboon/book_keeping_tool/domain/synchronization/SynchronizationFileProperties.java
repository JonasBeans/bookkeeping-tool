package be.jonasboon.book_keeping_tool.domain.synchronization;

import be.jonasboon.book_keeping_tool.domain.balance_posts.entity.BalanceSubPost;
import be.jonasboon.book_keeping_tool.domain.balance_posts.repository.BalanceRepository;
import be.jonasboon.book_keeping_tool.domain.balance_posts.repository.BalanceSubPostRepository;
import be.jonasboon.book_keeping_tool.domain.cost_centers.entity.CostCenter;
import be.jonasboon.book_keeping_tool.domain.cost_centers.repository.CostCenterRepository;
import be.jonasboon.book_keeping_tool.domain.synchronization.executors.restore.EntityHandler;
import be.jonasboon.book_keeping_tool.domain.synchronization.executors.restore.EntityHandlerDefault;
import be.jonasboon.book_keeping_tool.domain.synchronization.executors.restore.TransactionEntityHandler;
import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import be.jonasboon.book_keeping_tool.domain.transactions.repository.TransactionRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SynchronizationFileProperties {

    public List<SynchronizationFileProperty> PROPERTIES;

    private final TransactionEntityHandler transactionEntityHandler;

    private final TransactionRepository transactionRepository;
    private final CostCenterRepository costCenterRepository;
    private final BalanceRepository balanceRepository;
    private final BalanceSubPostRepository balanceSubPostRepository;

    public SynchronizationFileProperties(TransactionEntityHandler transactionEntityHandler, TransactionRepository transactionRepository, CostCenterRepository costCenterRepository, BalanceRepository balanceRepository, BalanceSubPostRepository balanceSubPostRepository) {
        this.transactionEntityHandler = transactionEntityHandler;
        this.transactionRepository = transactionRepository;
        this.costCenterRepository = costCenterRepository;
        this.balanceRepository = balanceRepository;
        this.balanceSubPostRepository = balanceSubPostRepository;
        load();
    }

    public void load() {
        PROPERTIES = List.of(
                new SynchronizationFileProperty<>("cost_centers.bkt", costCenterRepository, ParameterizedTypeReference.forType(CostCenter.class)),
                new SynchronizationFileProperty<>("balance_posts.bkt", balanceRepository, ParameterizedTypeReference.forType(BalanceSubPost.class)),
                new SynchronizationFileProperty<>("balance_sub_posts.bkt", balanceSubPostRepository, ParameterizedTypeReference.forType(BalanceSubPost.class)),
                new SynchronizationFileProperty<>("transactions.bkt", transactionRepository, transactionEntityHandler, ParameterizedTypeReference.forType(Transaction.class))
        );
    }

    public final List<String> getAllFileNames() {
        return PROPERTIES.stream().map(SynchronizationFileProperty::fileName).toList();
    }

   public record SynchronizationFileProperty<ENTITY>(
            String fileName,
            JpaRepository<ENTITY,?> repository,
            EntityHandler entityHandler,
            ParameterizedTypeReference<ENTITY> typeReference
    ) {
       SynchronizationFileProperty(
               String fileName,
               JpaRepository<ENTITY,?> repository,
               ParameterizedTypeReference<ENTITY> typeReference
       ) {this(fileName, repository, new EntityHandlerDefault(), typeReference);}
   }
}
