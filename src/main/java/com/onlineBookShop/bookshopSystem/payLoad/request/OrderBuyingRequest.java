package com.onlineBookShop.bookshopSystem.payLoad.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderBuyingRequest {

    private String bookName;
    private String authorName;
}
