package com.onlineBookShop.bookshopSystem.service;

import com.onlineBookShop.bookshopSystem.entity.BookBuyable;

import java.util.List;

public interface BookRedisService {
    List<BookBuyable> findAllBooks();
}
