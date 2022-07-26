package com.onlineBookShop.bookshopSystem.eWallet.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "EWALLET_HISTORY")
public class EWalletHistory {
    private long id;
    private long ownerId;
    private long bookId;
    private BigDecimal beforeBalance;
    private BigDecimal afterBalance;
    private LocalTime buyTime;
    private LocalDate buyDate;

    public EWalletHistory(long ownerId, long bookId, BigDecimal beforeBalance,
                          BigDecimal afterBalance, LocalTime buyTime, LocalDate buyDate) {
        this.ownerId = ownerId;
        this.bookId = bookId;
        this.beforeBalance = beforeBalance;
        this.afterBalance = afterBalance;
        this.buyTime = buyTime;
        this.buyDate = buyDate;
    }
}
