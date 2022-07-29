package com.onlineBookShop.bookshopSystem.service;

import com.onlineBookShop.bookshopSystem.entity.BookBuyable;
import com.onlineBookShop.bookshopSystem.payLoad.request.OrderBuyingRequest;
import com.onlineBookShop.bookshopSystem.payLoad.request.OrderDeleteRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;

import java.time.LocalDate;
import java.util.List;

public interface BookShopService {
    BaseResponse getOrderList(LocalDate date);
    BaseResponse getBookList();
    BaseResponse buyBook(OrderBuyingRequest order);
    BaseResponse deleteOrder(OrderDeleteRequest orderDeleteRequest);
}
