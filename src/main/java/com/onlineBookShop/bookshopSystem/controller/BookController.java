package com.onlineBookShop.bookshopSystem.controller;

import com.onlineBookShop.bookshopSystem.entity.Book;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;


    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("")
    @RolesAllowed({"user","admin"})
    BaseResponse getBooks(@RequestParam(defaultValue = "1")Integer pageNo,
                          @RequestParam(defaultValue = "10")Integer pageSize,
                          @RequestParam(defaultValue = "id")String sortBy){
        return bookService.findAllBook(pageNo,pageSize,sortBy);
    }

    @GetMapping("/name/")
    @RolesAllowed({"user","admin"})
    BaseResponse getBookByID(@RequestParam String name){
        return bookService.getBookByName(name);
    }

    @PostMapping("")
    @RolesAllowed("admin")
    BaseResponse uploadBook(@RequestBody Book book){
        return bookService.createBook(book);
    }

    @GetMapping("/author")
    @RolesAllowed({"user","admin"})
    BaseResponse getBooksByAuthorName(@RequestParam String name){
        return bookService.getBooksByAuthorName(name);
    }

    @GetMapping("/method")
    @RolesAllowed({"user","admin"})
    public BaseResponse sortingBookList(@RequestParam(name = "method",required = false) Integer method){
        return bookService.sortingBookList(method);
    }

    @PutMapping("/{id}")
    @RolesAllowed("admin")
    public BaseResponse updateBook(@PathVariable Long id, @RequestBody Book book){
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("admin")
    public BaseResponse deleteBook(@PathVariable Long id) { return bookService.deleteBook(id);}

}


