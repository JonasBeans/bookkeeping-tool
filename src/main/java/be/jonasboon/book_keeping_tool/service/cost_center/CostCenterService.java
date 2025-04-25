package be.jonasboon.book_keeping_tool.service.cost_center;

import be.jonasboon.book_keeping_tool.persistence.repository.CostCenterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CostCenterService {

    private final CostCenterRepository costCenterRepository;

}
