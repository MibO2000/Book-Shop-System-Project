package com.onlineBookShop.bookshopSystem.payLoad.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EWalletInfoResponse {
    private String accountName;
    private BigDecimal balance;
}
