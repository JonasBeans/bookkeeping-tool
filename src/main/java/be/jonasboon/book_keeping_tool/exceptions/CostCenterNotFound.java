package be.jonasboon.book_keeping_tool.exceptions;

public class CostCenterNotFound extends RuntimeException{
    public CostCenterNotFound() {
        super("Cost center doesn't exist");
    }
}
