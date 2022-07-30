package com.onlineBookShop.bookshopSystem.payLoad.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCreateRequest {
    private String bookName;
    private String authorName;
    private LocalDate dateOfRelease;
    private BigDecimal price;
    private Integer bookCount;
}
