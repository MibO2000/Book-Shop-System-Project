package com.onlineBookShop.bookshopSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@RedisHash("bookBuyable")
public class BookBuyable implements Serializable {
    @Serial
    private static final long serialVersionUID = -8371663472488393374L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long bookId;
    private String bookName;
    private String authorName;
    private BigDecimal price;

    public BookBuyable(Long bookId, String bookName, String authorName, BigDecimal price) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.price = price;
    }
}
