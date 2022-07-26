package com.onlineBookShop.bookshopSystem.service;

import com.onlineBookShop.bookshopSystem.entity.Book;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;

import java.math.BigDecimal;
import java.util.List;

public interface BookService {
    BaseResponse findAllBook(Integer pageNo, Integer pageSize, String sortBy);
    BaseResponse createBook(Book book);
    BaseResponse getBooksByAuthorId(String name);
    BaseResponse sortingBookList(Integer method);
    BaseResponse updateBook(Long id, Book book);
    BaseResponse deleteBook(Long id);
    BaseResponse getBookByID(Long id);
    List<Book> findBookStock();
    Boolean updateBookCount(Integer count, Long id);
    Integer findBookAvailability(Long id);
    BigDecimal getBookPrice(Long id);
    Book findBookById(Long id);
}
