package be.jonasboon.book_keeping_tool.DTO;

import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
public record GetBalanceInformationDTO(
        String title,
        List<SubPostDTO> subPosts
) {

    public GetBalanceInformationDTO(String title) {
        this(title, new ArrayList<>());
    }

    @Builder
    public record SubPostDTO(
            String title,
            double amount
    ) {
    }

    ;
}
