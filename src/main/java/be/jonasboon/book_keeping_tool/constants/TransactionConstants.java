package be.jonasboon.book_keeping_tool.constants;

import java.util.List;

public class TransactionConstants {
    public static final String SEPERATOR = "============================";
    public static final Integer ACCOUNTING_WORKSHEET = 2;
    public static final List<String> POSSIBLE_COST_CENTERS = List.of("1", "2", "3");

    public static class CellValues{
       public static final Integer DATE_CELL = 0;
        public static final Integer AMOUNT_CELL = 6;
        public static final Integer NAME_OTHER_PARTY_CELL = 1;
    }
}
