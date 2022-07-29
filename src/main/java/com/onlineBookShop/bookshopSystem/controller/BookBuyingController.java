package com.onlineBookShop.bookshopSystem.controller;

import com.onlineBookShop.bookshopSystem.entity.BookBuyable;
import com.onlineBookShop.bookshopSystem.payLoad.request.OrderBuyingRequest;
import com.onlineBookShop.bookshopSystem.payLoad.request.OrderDeleteRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.payLoad.response.BookBuyingResponse;
import com.onlineBookShop.bookshopSystem.service.BookShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

    @PostMapping("/order")
    @RolesAllowed("user")
    public BaseResponse buyBook(@RequestBody OrderBuyingRequest order){
        return bookShopService.buyBook(order);
    }

    @DeleteMapping("/order")
    @RolesAllowed("user")
    public BaseResponse deleteOrder(@RequestBody OrderDeleteRequest orderDeleteRequest){
        return bookShopService.deleteOrder(orderDeleteRequest);
    }

}
