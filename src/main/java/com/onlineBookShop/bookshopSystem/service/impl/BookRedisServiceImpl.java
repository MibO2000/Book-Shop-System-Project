package com.onlineBookShop.bookshopSystem.service.impl;

import com.onlineBookShop.bookshopSystem.entity.Book;
import com.onlineBookShop.bookshopSystem.entity.BookBuyable;
import com.onlineBookShop.bookshopSystem.repository.BookRedisRepository;
import com.onlineBookShop.bookshopSystem.service.AuthorService;
import com.onlineBookShop.bookshopSystem.service.BookRedisService;
import com.onlineBookShop.bookshopSystem.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BookRedisServiceImpl implements BookRedisService {
    private final BookService bookService;
    private final AuthorService authorService;
    private final BookRedisRepository bookRedisRepository;

    @Autowired
    public BookRedisServiceImpl(BookService bookService, AuthorService authorService,
                                BookRedisRepository bookRedisRepository) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.bookRedisRepository = bookRedisRepository;
    }

    @Override
    @Cacheable(value = "bookBuyable")
    public List<BookBuyable> findAllBooks() {
        try {
            List<BookBuyable> bookList = bookRedisRepository.findAll();
            if (bookList.isEmpty()) {
                List<Book> bookStockList = bookService.findBookStock();
                Long id = 1l;
                for (Book book : bookStockList) {
                    bookList.add(new BookBuyable(id,
                            book.getId(), book.getName(),
                            authorService.getAuthorNameById(book.getAuthorId()),
                            book.getPrice()
                    ));
                    id++;
                }
            }
            return bookList;
        }catch (Exception e){
            log.error("Error: {}",e.getMessage());
            return null;
        }
    }
}
