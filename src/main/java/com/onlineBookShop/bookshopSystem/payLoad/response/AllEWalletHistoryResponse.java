package com.onlineBookShop.bookshopSystem.payLoad.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllEWalletHistoryResponse {
    private Long id;
    private String userName;
    private String bookName;
    private BigDecimal beforeBalance;
    private BigDecimal afterBalance;
    private LocalTime buyTime;
    private LocalDate buyDate;
}
