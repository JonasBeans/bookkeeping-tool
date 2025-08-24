package be.jonasboon.book_keeping_tool.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AddCostCenterDTO(
        @JsonAlias("title")
        String costCenter,
        Boolean isCost
) { }
