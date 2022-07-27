package com.onlineBookShop.bookshopSystem.service;

import com.onlineBookShop.bookshopSystem.payLoad.request.OrderDeleteRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;

import java.time.LocalDate;

public interface BookShopService {
    BaseResponse getOrderList(LocalDate date);
    BaseResponse getBookList();
    BaseResponse buyBook(Long bookId);
    BaseResponse deleteOrder(OrderDeleteRequest orderDeleteRequest);
}
