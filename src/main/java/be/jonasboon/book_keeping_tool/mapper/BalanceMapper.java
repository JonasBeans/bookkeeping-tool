package be.jonasboon.book_keeping_tool.mapper;

import be.jonasboon.book_keeping_tool.DTO.AddBalancePostDTO;
import be.jonasboon.book_keeping_tool.DTO.GetBalanceInformationDTO;
import be.jonasboon.book_keeping_tool.persistence.entity.BalancePost;
import be.jonasboon.book_keeping_tool.persistence.entity.BalanceSubPost;

public class BalanceMapper {

    public static GetBalanceInformationDTO toDTO(BalancePost balancePost) {
        return GetBalanceInformationDTO.builder()
                .title(balancePost.getTitle())
                .subPosts(balancePost.getBalanceSubPosts().stream().map(BalanceMapper::toDTO).toList())
                .build();
    }

    public static GetBalanceInformationDTO.SubPostDTO toDTO(BalanceSubPost entity) {
        return GetBalanceInformationDTO.SubPostDTO.builder()
                .title(entity.getTitle())
                .amount(entity.getAmount())
                .build();
    }

    public static BalanceSubPost toEntity(BalancePost balancePost,AddBalancePostDTO dto) {
        return BalanceSubPost.builder()
                .title(dto.subPost().title())
                .amount(dto.subPost().amount())
                .balancePost(balancePost)
                .build();
    }
}
