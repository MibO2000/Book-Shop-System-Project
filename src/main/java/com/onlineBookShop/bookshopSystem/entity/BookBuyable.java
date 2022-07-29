package com.onlineBookShop.bookshopSystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@RedisHash("book")
public class BookBuying implements Serializable {
    @Id
    private Long bookId;
    private String bookName;
    private String authorName;
    private BigDecimal price;
}
