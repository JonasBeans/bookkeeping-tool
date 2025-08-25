package be.jonasboon.book_keeping_tool.DTO;

public record AddBalancePostDTO(
        String title,
        GetBalanceInformationDTO.SubPostDTO subPost) {
}
