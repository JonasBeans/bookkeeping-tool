package be.jonasboon.book_keeping_tool.domain.balance_posts.DTO;

public record AddBalancePostDTO(
        String title,
        GetBalanceInformationDTO.SubPostDTO subPost) {
}
