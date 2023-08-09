package be.javabeans.mapper;

import be.javabeans.DTO.TransactionDTO;
import be.javabeans.model.Transaction;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TransactionMapper {

    public static Transaction toTransactionWithCostCenter(TransactionDTO dto, String costCenterIndex){
        return Transaction.builder()
                .withBookDate(dto.getBookDate())
                .withTransactionDate(dto.getTransactionDate())
                .withAmount(dto.getAmount())
                .withNameOtherParty(dto.getNameOtherParty())
                .withCostCenterIndex(Integer.valueOf(costCenterIndex))
                .build();
    }
}
