package be.jonasboon.book_keeping_tool.service.balance;

import be.jonasboon.book_keeping_tool.DTO.AddBalancePostDTO;
import be.jonasboon.book_keeping_tool.DTO.GetBalanceInformationDTO;
import be.jonasboon.book_keeping_tool.DTO.UpdateBalancePostDTO;
import be.jonasboon.book_keeping_tool.mapper.BalanceMapper;
import be.jonasboon.book_keeping_tool.persistence.entity.BalancePost;
import be.jonasboon.book_keeping_tool.persistence.entity.BalanceSubPost;
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
        BalancePost balancePost = balanceRepository.findByTitle(balancePostTitle)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Balance post not found: %s", balancePostTitle)));
        return BalanceMapper.toDTO(balancePost);
    }

    public void saveBalancePost(AddBalancePostDTO dto) {
        BalancePost balancePost = balanceRepository.findByTitle(dto.title())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Balance post not found: %s", dto.title())));
        BalanceSubPost entity = BalanceMapper.toEntity(dto);
        balancePost.getBalanceSubPosts().add(entity);
        balanceRepository.save(balancePost);
    }

    public void updateBalancePost(UpdateBalancePostDTO dto) {
        BalancePost updatedEntity =
                balanceRepository
                        .findByTitle(dto.title())
                        .orElseThrow(() -> new IllegalArgumentException(String.format("Balance post not found: %s", dto.title())));

        updatedEntity
                .getBalanceSubPosts()
                .stream()
                .collect(Collectors.toMap(
                        BalanceSubPost::getTitle,
                        identity()
                ))
                .computeIfPresent(dto.subPost().title(), (title, entity) -> {
                    entity.setAmount(dto.subPost().amount());
                    return entity;
                });

        balanceRepository.save(updatedEntity);
    }

    public void deleteBalancePost(String balancePostTitle, String subPostTitle) {
        BalancePost entity =
                balanceRepository
                        .findByTitle(balancePostTitle)
                        .orElseThrow(() -> new IllegalArgumentException(String.format("Balance post not found: %s", subPostTitle)));

        boolean isASubPostRemoved = entity.getBalanceSubPosts().removeIf(balanceSubPost -> subPostTitle.equals(balanceSubPost.getTitle()));
        if (!isASubPostRemoved)
            throw new IllegalArgumentException(String.format("Sub post not found: %s", subPostTitle));
        else
            log.info("Deleting sub post: {}", subPostTitle);
        balanceRepository.save(entity);
    }
}
