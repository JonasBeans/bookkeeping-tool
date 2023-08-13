package be.javabeans.exceptions;

public class CostCenterNotFound extends RuntimeException{
    public CostCenterNotFound() {
        super("Cost center doesn't exist");
    }
}
