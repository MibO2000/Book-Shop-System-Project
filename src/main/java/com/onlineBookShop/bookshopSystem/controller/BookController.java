package com.onlineBookShop.bookshopSystem.controller;

import com.onlineBookShop.bookshopSystem.payLoad.request.BookCreateRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/books")
@CrossOrigin
public class BookController {
    private final BookService bookService;


    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("")
    @CrossOrigin
    @RolesAllowed({"user","admin"})
    BaseResponse getBooks(@RequestParam(defaultValue = "1")Integer pageNo,
                          @RequestParam(defaultValue = "10")Integer pageSize,
                          @RequestParam(defaultValue = "id")String sortBy){
        return bookService.findAllBook(pageNo,pageSize,sortBy);
    }

    @GetMapping("/name/")
    @CrossOrigin
    @RolesAllowed({"user","admin"})
    BaseResponse getBookByID(@RequestParam String name){
        return bookService.getBookByName(name);
    }

    @PostMapping("")
    @CrossOrigin
    @RolesAllowed("admin")
    BaseResponse uploadBook(@RequestBody BookCreateRequest request){
        return bookService.createBook(request);
    }

    @GetMapping("/author")
    @CrossOrigin
    @RolesAllowed({"user","admin"})
    BaseResponse getBooksByAuthorName(@RequestParam String name){
        return bookService.getBooksByAuthorName(name);
    }

    @GetMapping("/method")
    @CrossOrigin
    @RolesAllowed({"user","admin"})
    public BaseResponse sortingBookList(@RequestParam(name = "method",required = false) Integer method){
        return bookService.sortingBookList(method);
    }

    @PutMapping("/{name}")
    @CrossOrigin
    @RolesAllowed("admin")
    public BaseResponse updateBook(@PathVariable String name, @RequestBody BookCreateRequest request){
        return bookService.updateBook(name, request);
    }

    @DeleteMapping("/delete")
    @CrossOrigin
    @RolesAllowed("admin")
    public BaseResponse deleteBook(@RequestParam String name) { return bookService.deleteBook(name);}

}


