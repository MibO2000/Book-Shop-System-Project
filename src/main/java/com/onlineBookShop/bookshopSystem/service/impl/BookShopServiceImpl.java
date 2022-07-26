package com.onlineBookShop.bookshopSystem.service.impl;

import com.onlineBookShop.bookshopSystem.eWallet.entity.EWalletHistory;
import com.onlineBookShop.bookshopSystem.eWallet.service.EWalletHistoryService;
import com.onlineBookShop.bookshopSystem.eWallet.service.EWalletInfoService;
import com.onlineBookShop.bookshopSystem.entity.Book;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.service.BookService;
import com.onlineBookShop.bookshopSystem.service.BookShopService;
import com.onlineBookShop.bookshopSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.util.List;

@Service
@Slf4j
public class BookShopServiceImpl implements BookShopService {

    private final EWalletHistoryService eWalletHistoryService;
    private final BookService bookService;
    private final UserService userService;
    private final EWalletInfoService eWalletInfoService;

    @Autowired
    public BookShopServiceImpl(EWalletHistoryService eWalletHistoryService, BookService bookService, UserService userService, EWalletInfoService eWalletInfoService) {
        this.eWalletHistoryService = eWalletHistoryService;
        this.bookService = bookService;
        this.userService = userService;
        this.eWalletInfoService = eWalletInfoService;
    }

    @Override
    public BaseResponse getOrderList(LocalDate date) {
        try{
            if(date==null){
                date = LocalDate.now();
            }
            List<EWalletHistory> eWalletHistories = eWalletHistoryService.getHistoryByDate(date);
            return new BaseResponse("Here is the order List",eWalletHistories,false,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to fetch order list",null,false, LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse getBookList() {
        try{
            List<Book> bookList = bookService.findBookStock();
            return new BaseResponse("Here is the book List",bookList,true,LocalDateTime.now());
        }catch (Exception e){
            log.error("Error: "+e);
            return new BaseResponse("Fail to get book list",null,false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse buyBook(Long bookId) {
        try{
            Integer count = bookService.findBookAvailability(bookId);
            if (count < 1){
                return new BaseResponse("Book Out of stock",null,false,LocalDateTime.now());
            }
            BigDecimal balance = eWalletInfoService.getBalance();
            BigDecimal price = bookService.getBookPrice(bookId);
            if (balance.compareTo(price) < 0){
                return new BaseResponse("Insufficient balance",bookService.getBookByID(bookId),false,LocalDateTime.now());
            }
            if(!bookService.updateBookCount(count - 1, bookId)){
                return new BaseResponse("Book Count not updated",null,false,LocalDateTime.now());
            }
            if(!eWalletInfoService.updateBalanceAfterBuying(price)){
                return new BaseResponse("Fail to update balance",null,false,LocalDateTime.now());
            }
            boolean result = eWalletHistoryService.createHistory(new EWalletHistory(
                    userService.getUserId(), bookId, balance,
                    balance.subtract(price), LocalTime.now(), LocalDate.now()
            ));
            if (!result){
                return new BaseResponse("Fail to get into history table",null,false,LocalDateTime.now());
            }
            return new BaseResponse("Buy successful", eWalletHistoryService.getHistoryByBookID(bookId),true,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to buy book",null,false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse deleteOrder(Long bookId) {
        EWalletHistory eWalletHistory = eWalletHistoryService.getHistoryByBookID(bookId);
        LocalDate buyDate = eWalletHistory.getBuyDate();
        int difference = Period.between(buyDate,LocalDate.now()).getDays();
        if (difference > 1){
            return new BaseResponse("Cannot cancel the order after 1 day",null,false,LocalDateTime.now());
        }

        if(eWalletHistoryService.deleteHistory(buyDate, bookId)){
            if(eWalletInfoService.updateBalanceAfterDeleting(eWalletHistory.getBeforeBalance().subtract(eWalletHistory.getAfterBalance()))) {
                return new BaseResponse("Order deleted", eWalletHistory, true, LocalDateTime.now());
            }
            log.error("Fail to add money");
            return new BaseResponse("Fail to add money",eWalletHistory,false,LocalDateTime.now());
        }
        return new BaseResponse("Fail to delete order",eWalletHistory,false,LocalDateTime.now());
    }
}
