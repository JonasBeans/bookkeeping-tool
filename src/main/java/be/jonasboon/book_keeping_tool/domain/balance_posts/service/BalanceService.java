package be.jonasboon.book_keeping_tool.domain.balance_posts.service;

import be.jonasboon.book_keeping_tool.domain.balance_posts.DTO.AddBalancePostDTO;
import be.jonasboon.book_keeping_tool.domain.balance_posts.DTO.GetBalanceInformationDTO;
import be.jonasboon.book_keeping_tool.domain.balance_posts.DTO.UpdateBalancePostDTO;
import be.jonasboon.book_keeping_tool.domain.balance_posts.entity.BalancePost;
import be.jonasboon.book_keeping_tool.domain.balance_posts.entity.BalanceSubPost;
import be.jonasboon.book_keeping_tool.domain.balance_posts.mapper.BalanceMapper;
import be.jonasboon.book_keeping_tool.domain.balance_posts.repository.BalanceRepository;
import be.jonasboon.book_keeping_tool.domain.balance_posts.repository.BalanceSubPostRepository;
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
    private final BalanceSubPostRepository balanceSubPostRepository;

    public GetBalanceInformationDTO findBalancePostByTitle(String balancePostTitle) {
        BalancePost balancePost = balanceRepository.findByTitle(balancePostTitle)
                .orElseThrow(() -> new IllegalArgumentException(String.format("Balance post not found: %s", balancePostTitle)));
        return BalanceMapper.toDTO(balancePost);
    }

    public void saveBalanceSubPost(AddBalancePostDTO dto) {
        BalancePost balancePost = balanceRepository.findByTitle(dto.title())
                .orElseThrow(() -> new IllegalArgumentException(String.format("Balance post not found: %s", dto.title())));

        BalanceSubPost entity = BalanceMapper.toEntity(balancePost, dto);
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

    public void deleteBalancePost(String subPostTitle) {
        balanceSubPostRepository.deleteById(subPostTitle);
    }
}
