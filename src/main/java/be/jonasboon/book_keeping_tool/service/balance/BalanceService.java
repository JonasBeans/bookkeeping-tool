package be.jonasboon.book_keeping_tool.service.balance;

import be.jonasboon.book_keeping_tool.DTO.AddBalancePostDTO;
import be.jonasboon.book_keeping_tool.DTO.GetBalanceInformationDTO;
import be.jonasboon.book_keeping_tool.DTO.UpdateBalancePostDTO;
import be.jonasboon.book_keeping_tool.mapper.BalanceMapper;
import be.jonasboon.book_keeping_tool.persistence.entity.BalancePostEntity;
import be.jonasboon.book_keeping_tool.persistence.repository.BalanceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static java.util.function.UnaryOperator.identity;

@Slf4j
@Service
@AllArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;

    public GetBalanceInformationDTO findBalancePostByTitle(String balancePostTitle) {
        BalancePostEntity balancePost = balanceRepository.findByTitle(balancePostTitle)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Balance post not found: %s", balancePostTitle)));
        return BalanceMapper.toDTO(balancePost);
    }

    public void saveBalancePost(AddBalancePostDTO dto) {
        BalancePostEntity balancePost = balanceRepository.findByTitle(dto.title())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Balance post not found: %s", dto.title())));
        BalancePostEntity.SubPostEntity entity = BalanceMapper.toEntity(dto);
        balancePost.getSubPosts().add(entity);
        balanceRepository.save(balancePost);
    }

    public void updateBalancePost(UpdateBalancePostDTO dto) {
        BalancePostEntity updatedEntity =
                balanceRepository
                        .findByTitle(dto.title())
                        .orElseThrow(() -> new IllegalArgumentException(String.format("Balance post not found: %s", dto.title())));

        updatedEntity
                .getSubPosts()
                .stream()
                .collect(Collectors.toMap(
                        BalancePostEntity.SubPostEntity::getTitle,
                        identity()
                ))
                .computeIfPresent(dto.subPost().title(), (title, entity) -> {
                    entity.setAmount(dto.subPost().amount());
                    return entity;
                });

        balanceRepository.save(updatedEntity);
    }

    public void deleteBalancePost(String balancePostTitle, String subPostTitle) {
        BalancePostEntity entity =
                balanceRepository
                        .findByTitle(balancePostTitle)
                        .orElseThrow(() -> new IllegalArgumentException(String.format("Balance post not found: %s", subPostTitle)));

        boolean isASubPostRemoved = entity.getSubPosts().removeIf(subPost -> subPostTitle.equals(subPost.getTitle()));
        if (!isASubPostRemoved)
            throw new IllegalArgumentException(String.format("Sub post not found: %s", subPostTitle));
        else
            log.info("Deleting sub post: {}", subPostTitle);
        balanceRepository.save(entity);
    }
}
