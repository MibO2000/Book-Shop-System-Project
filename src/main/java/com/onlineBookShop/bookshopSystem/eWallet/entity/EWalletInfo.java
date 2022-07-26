package com.onlineBookShop.bookshopSystem.eWallet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EWALLET_INFO")
public class EWalletInfo {
    private long id;
    private long ownerId;
    private String accountName;
    private BigDecimal balance;
    private LocalDateTime createAt;

    public EWalletInfo(long ownerId, String accountName, LocalDateTime createAt, BigDecimal balance) {
        this.ownerId = ownerId;
        this.accountName = accountName;
        this.createAt = createAt;
        this.balance = balance;
    }
}
