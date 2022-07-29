package com.onlineBookShop.bookshopSystem.entity;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String name;
    @Column
    private long authorId;
    @Column
    private LocalDate dateOfRelease;
    @Column
    private BigDecimal price;
    @Column
    private Integer bookCount;
    @Column
    private LocalDateTime createdAt;

    public Book(String name, long authorId, LocalDate dateOfRelease, BigDecimal price,
                Integer bookCount, LocalDateTime createdAt) {
        this.name = name;
        this.authorId = authorId;
        this.dateOfRelease = dateOfRelease;
        this.price = price;
        this.bookCount = bookCount;
        this.createdAt = createdAt;
    }
}
