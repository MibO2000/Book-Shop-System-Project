package com.onlineBookShop.bookshopSystem.service;

import com.onlineBookShop.bookshopSystem.entity.Book;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.payLoad.response.BookResponse;

import java.math.BigDecimal;
import java.util.List;

public interface BookService {
    BaseResponse findAllBook(Integer pageNo, Integer pageSize, String sortBy);
    BaseResponse createBook(Book book);
    BaseResponse getBooksByAuthorName(String name);
    BaseResponse sortingBookList(Integer method);
    BaseResponse updateBook(Long id, Book book);
    BaseResponse deleteBook(Long id);
    BaseResponse getBookByName(String name);
    List<Book> findBookStock();
    Boolean updateBookCount(Integer count, String bookName);
    Integer findBookAvailability(Long bookId,Long authorId);
    BigDecimal getBookPrice(Long id);
    Book findBookById(Long id);
    Book findBookByName(String name);

    BookResponse convertBookResponse(Book book);
}
