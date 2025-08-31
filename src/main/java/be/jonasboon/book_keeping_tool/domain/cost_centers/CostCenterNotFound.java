package be.jonasboon.book_keeping_tool.domain.cost_centers;

public class CostCenterNotFound extends RuntimeException{
    public CostCenterNotFound() {
        super("Cost center doesn't exist");
    }
}
