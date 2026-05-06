package be.jonasboon.book_keeping_tool.domain.transactions.classification;

import be.jonasboon.book_keeping_tool.domain.cost_centers.entity.CostCenter;
import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;

import java.util.List;

public interface CostCenterClassifier {

    List<CostCenterPrediction> predict(List<Transaction> transactions, List<CostCenter> costCenters);
}
