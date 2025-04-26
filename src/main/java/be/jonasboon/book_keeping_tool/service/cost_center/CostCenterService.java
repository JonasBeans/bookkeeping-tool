package be.jonasboon.book_keeping_tool.service.cost_center;

import be.jonasboon.book_keeping_tool.DTO.CostCenterDTO;
import be.jonasboon.book_keeping_tool.mapper.CostCenterMapper;
import be.jonasboon.book_keeping_tool.persistence.repository.CostCenterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CostCenterService {

    private final CostCenterRepository costCenterRepository;

    public List<CostCenterDTO> getAllCostCenters() {
        return costCenterRepository.findAll().stream().map(CostCenterMapper::fromEntity).toList();
    }
}
