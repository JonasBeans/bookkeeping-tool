package be.jonasboon.book_keeping_tool.controllers;

import be.jonasboon.book_keeping_tool.DTO.AccumulatedAmountsDTO;
import be.jonasboon.book_keeping_tool.DTO.CostCenterDTO;
import be.jonasboon.book_keeping_tool.service.cost_center.CostCenterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("api/cost-centers")
@AllArgsConstructor
@RestController
public class CostCenterController {

    private final CostCenterService costCenterService;

    @GetMapping("/all")
    public List<CostCenterDTO> getAllCostCenters() {
        return costCenterService.getAllCostCenters();
    }

    @GetMapping("/accumulated-amounts")
    public AccumulatedAmountsDTO getAccumulatedAmounts() {
        return costCenterService.getAccumulatedAmounts();
    }
}
