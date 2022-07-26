package com.onlineBookShop.bookshopSystem.payLoad.request;

import com.onlineBookShop.bookshopSystem.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationRequest {
    private User user;
    private BigDecimal balance;
}
