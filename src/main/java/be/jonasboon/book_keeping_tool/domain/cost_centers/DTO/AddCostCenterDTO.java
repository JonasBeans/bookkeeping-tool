package be.jonasboon.book_keeping_tool.domain.cost_centers.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;

public record AddCostCenterDTO(
        @JsonAlias("title")
        String costCenter,
        Boolean isCost
) { }
