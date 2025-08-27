package be.jonasboon.book_keeping_tool.mapper;

import be.jonasboon.book_keeping_tool.DTO.AddBalancePostDTO;
import be.jonasboon.book_keeping_tool.DTO.GetBalanceInformationDTO;
import be.jonasboon.book_keeping_tool.persistence.entity.BalancePostEntity;

public class BalanceMapper {

    public static GetBalanceInformationDTO toDTO(BalancePostEntity balancePost) {
        return GetBalanceInformationDTO.builder()
                .title(balancePost.getTitle())
                .subPosts(balancePost.getSubPosts().stream().map(BalanceMapper::toDTO).toList())
                .build();
    }

    public static GetBalanceInformationDTO.SubPostDTO toDTO(BalancePostEntity.SubPostEntity entity) {
        return GetBalanceInformationDTO.SubPostDTO.builder()
                .title(entity.getTitle())
                .amount(entity.getAmount())
                .build();
    }

    public static BalancePostEntity.SubPostEntity toEntity(AddBalancePostDTO dto) {
        return BalancePostEntity.SubPostEntity.builder()
                .title(dto.subPost().title())
                .amount(dto.subPost().amount())
                .build();
    }
}
