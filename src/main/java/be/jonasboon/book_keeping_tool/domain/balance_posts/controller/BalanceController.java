package be.jonasboon.book_keeping_tool.domain.balance_posts.controller;

import be.jonasboon.book_keeping_tool.domain.balance_posts.DTO.AddBalancePostDTO;
import be.jonasboon.book_keeping_tool.domain.balance_posts.DTO.GetBalanceInformationDTO;
import be.jonasboon.book_keeping_tool.domain.balance_posts.DTO.UpdateBalancePostDTO;
import be.jonasboon.book_keeping_tool.domain.balance_posts.service.BalanceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/balance")
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping
    public GetBalanceInformationDTO getBalanceInformation(@RequestParam("title") String balancePostTitle) {
        if (balancePostTitle.isEmpty()) throw new IllegalArgumentException();

        return balanceService.findBalancePostByTitle(balancePostTitle);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addBalanceInformation(@RequestBody AddBalancePostDTO dto) {
        if (dto == null || dto.title().isEmpty()) throw new IllegalArgumentException("Balance post title cannot be empty");
        if (dto.subPost() == null || dto.subPost().title().isEmpty()) throw new IllegalArgumentException("Sub post title cannot be empty");
        balanceService.saveBalanceSubPost(dto);
        return ResponseEntity.ok("Successfully added new balance post");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateBalanceInformation(@RequestBody UpdateBalancePostDTO dto) {
        if (dto == null || dto.title().isEmpty()) throw new IllegalArgumentException();
        balanceService.updateBalancePost(dto);
        return ResponseEntity.ok(String.format("Successfully updated balance post: %s", dto.title()));
    }

    @DeleteMapping
    public ResponseEntity<String> deleteBalanceInformation(@RequestParam("subPostTitle") String subPostTitle) {
        if (subPostTitle.isEmpty()) throw new IllegalArgumentException();
        balanceService.deleteBalancePost(subPostTitle);
        return ResponseEntity.ok(String.format("Successfully deleted balance sub post: %s", subPostTitle));
    }


}
