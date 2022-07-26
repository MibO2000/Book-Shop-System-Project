package com.onlineBookShop.bookshopSystem.payLoad.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDeleteRequest {
    private String bookName;
    private Long orderId;
}
