package be.jonasboon.book_keeping_tool.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
@AllArgsConstructor
@Getter@Setter
public class Transaction {

    private final LocalDate date;
}
