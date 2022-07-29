package com.onlineBookShop.bookshopSystem.payLoad.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookResponse {
    private String name;
    private String authorName;
    private LocalDate dateOfRelease;
    private BigDecimal price;
    private Integer bookCount;
}
