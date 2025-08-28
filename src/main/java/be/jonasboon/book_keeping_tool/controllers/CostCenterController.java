package be.jonasboon.book_keeping_tool.controllers;

import be.jonasboon.book_keeping_tool.DTO.AccumulatedAmountsDTO;
import be.jonasboon.book_keeping_tool.DTO.AddCostCenterDTO;
import be.jonasboon.book_keeping_tool.DTO.CostCenterDTO;
import be.jonasboon.book_keeping_tool.service.cost_center.CostCenterService;
import be.jonasboon.book_keeping_tool.utils.ExceptionUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
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

    @PostMapping("/add")
    public ResponseEntity addCostCenter(@RequestBody AddCostCenterDTO costCenterDTO) {
        log.info("Received request to add cost center: {}", costCenterDTO);
        try {
            costCenterService.addCostCenter(costCenterDTO);
        } catch (CostCenterService.CostCenterException e) {
            log.error("Error adding cost center: {}", e.getMessage());
            return ExceptionUtils.sendExceptionResponse(e, 500);
        }
        return ResponseEntity.ok().build();
    }
}
