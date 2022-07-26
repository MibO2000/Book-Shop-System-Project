package com.onlineBookShop.bookshopSystem.controller;

import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.service.BookShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDate;

@RestController
@Slf4j
@RequestMapping("bookshop")
public class BookBuyingController {
    private final BookShopService bookShopService;

    @Autowired
    public BookBuyingController(BookShopService bookShopService) {
        this.bookShopService = bookShopService;
    }

    @GetMapping("/orderList")
    @RolesAllowed({"admin","user"})
    public BaseResponse getOrderList(@RequestParam(required = false)LocalDate date){
        return bookShopService.getOrderList(date);
    }

    @GetMapping("/books")
    @RolesAllowed({"admin","user"})
    public BaseResponse getBookList(){
        return bookShopService.getBookList();
    }

    @PostMapping("/order/{id}")
    @RolesAllowed({"admin","user"})
    public BaseResponse buyBook(@PathVariable Long id){
        return bookShopService.buyBook(id);
    }

    @DeleteMapping("/order/{id}")
    @RolesAllowed({"admin","user"})
    public BaseResponse deleteOrder(@PathVariable Long id){
        return bookShopService.deleteOrder(id);
    }

}
