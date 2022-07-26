package com.onlineBookShop.bookshopSystem.payLoad.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceUpdateRequest {
    private BigDecimal balance;
    private String name;
}
