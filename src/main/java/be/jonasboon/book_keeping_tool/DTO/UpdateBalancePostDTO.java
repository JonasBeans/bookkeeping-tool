package be.jonasboon.book_keeping_tool.DTO;

public record UpdateBalancePostDTO(
        String title,
        GetBalanceInformationDTO.SubPostDTO subPost) {
}
