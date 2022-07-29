package com.onlineBookShop.bookshopSystem.service.impl;

import com.onlineBookShop.bookshopSystem.eWallet.entity.EWalletHistory;
import com.onlineBookShop.bookshopSystem.eWallet.service.EWalletHistoryService;
import com.onlineBookShop.bookshopSystem.eWallet.service.EWalletInfoService;
import com.onlineBookShop.bookshopSystem.entity.BookBuyable;
import com.onlineBookShop.bookshopSystem.payLoad.request.OrderBuyingRequest;
import com.onlineBookShop.bookshopSystem.payLoad.request.OrderDeleteRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.payLoad.response.BookBuyingResponse;
import com.onlineBookShop.bookshopSystem.service.*;
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

    private final BookRedisService bookRedisService;
    private final EWalletHistoryService eWalletHistoryService;
    private final BookService bookService;
    private final AuthorService authorService;
    private final UserService userService;
    private final EWalletInfoService eWalletInfoService;

    @Autowired
    public BookShopServiceImpl(BookRedisService bookRedisService, EWalletHistoryService eWalletHistoryService,
                               BookService bookService, AuthorService authorService, UserService userService,
                               EWalletInfoService eWalletInfoService) {
        this.bookRedisService = bookRedisService;
        this.eWalletHistoryService = eWalletHistoryService;
        this.bookService = bookService;
        this.authorService = authorService;
        this.userService = userService;
        this.eWalletInfoService = eWalletInfoService;
    }

    @Override
    public BaseResponse getOrderList(LocalDate date) {
        try {
            if (date == null) {
                date = LocalDate.now();
            }
            List<EWalletHistory> eWalletHistories = eWalletHistoryService.getHistoryByDate(date);
            return new BaseResponse("Here is the order List", eWalletHistories,
                    false, LocalDateTime.now());
        } catch (Exception e) {
            return new BaseResponse("Fail to fetch order list", e.getMessage(),
                    false, LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse getBookList() {
        try {
            return new BaseResponse("Here is the book list",
                    bookRedisService.findAllBooks().stream().map(this::convertBookListResponse),
                    true,LocalDateTime.now());
        } catch (Exception e) {
            log.error("Error: {}",e.getMessage());
            return null;
        }
    }

    @Override
    public BaseResponse buyBook(OrderBuyingRequest order) {
        try {
            String bookName = order.getBookName();
            Long authorId = authorService.getAuthorIdByName(order.getAuthorName());
            Long bookId = bookService.findBookByName(bookName).getId();
            Integer count = bookService.findBookAvailability(bookId,authorId);
            if (count < 1) {
                return new BaseResponse("Book Out of stock", null, false, LocalDateTime.now());
            }
            BigDecimal balance = eWalletInfoService.getBalance();
            BigDecimal price = bookService.getBookPrice(bookId);
            if (balance.compareTo(price) < 0) {
                return new BaseResponse("Insufficient balance",
                        bookService.convertBookResponse(bookService.findBookById(bookId)),
                        false, LocalDateTime.now());
            }
            if (!bookService.updateBookCount(count - 1, bookName)) {
                return new BaseResponse("Book Count not updated", null,
                        false, LocalDateTime.now());
            }
            BigDecimal newBalance = balance.subtract(price);
            return eWalletUpdateResponse(newBalance,bookId,balance);
        } catch (Exception e) {
            return new BaseResponse("Fail to buy book", e.getMessage(), false, LocalDateTime.now());
        }
    }
    private BaseResponse eWalletUpdateResponse(BigDecimal newBalance, Long bookId, BigDecimal balance) {
        try {
            if (!eWalletInfoService.updateBalanceAfterBuying(newBalance)) {
                return new BaseResponse("Fail to update balance", null,
                        false, LocalDateTime.now());
            }
            boolean result = eWalletHistoryService.createHistory(new EWalletHistory(
                    userService.getUserId(), bookId, balance,
                    newBalance, LocalTime.now(), LocalDate.now()
            ));
            if (!result) {
                return new BaseResponse("Fail to get into history table", null,
                        false, LocalDateTime.now());
            }
            return new BaseResponse("Buy successful", eWalletHistoryService.getEachEWalletHistory(),
                    true, LocalDateTime.now());
        }catch (Exception e){
            log.error("Error: {}",e.getMessage());
            return new BaseResponse("Fail in eWalletUpdateResponse", e.getMessage(),
                    false, LocalDateTime.now());
        }
    }
    @Override
    public BaseResponse deleteOrder(OrderDeleteRequest orderDeleteRequest) {
        try {
            String bookName = orderDeleteRequest.getBookName();
            Long orderId = orderDeleteRequest.getOrderId();
            EWalletHistory eWalletHistory = eWalletHistoryService.getHistoryByBookID(orderId);
            if (eWalletHistory == null) {
                return new BaseResponse("The order not exists",
                        String.format("orderID: %d\nbook name: %s", orderId, bookName),
                        false, LocalDateTime.now());
            }
            LocalDate buyDate = eWalletHistory.getBuyDate();
            BigDecimal balance = eWalletInfoService.getBalance();
            int difference = Period.between(buyDate, LocalDate.now()).getDays();
            if (difference > 1) {
                return new BaseResponse("Cannot cancel the order after 1 day", null,
                        false, LocalDateTime.now());
            }
            BigDecimal newBalance = balance.add(
                    eWalletHistory.getBeforeBalance().subtract(eWalletHistory.getAfterBalance()));
            return eWalletDeleteResponse(newBalance, orderId, bookName, eWalletHistory);
        }catch (Exception e){
            return new BaseResponse("Fail to delete order",e.getMessage(),false,LocalDateTime.now());
        }
    }

    private BaseResponse eWalletDeleteResponse(BigDecimal newBalance, Long orderId,
                                               String bookName, EWalletHistory eWalletHistory) {
        try {
            if (eWalletHistoryService.deleteHistory(orderId)) {
                if (eWalletInfoService.updateBalanceAfterDeleting(newBalance)) {
                    if (!bookService.updateBookCount(1, bookName)) {
                        return new BaseResponse("Book Count not updated", null,
                                false, LocalDateTime.now());
                    }
                    return new BaseResponse("Order deleted", eWalletHistory,
                            true, LocalDateTime.now());
                }
                log.error("Fail to add money");
                return new BaseResponse("Fail to add money", eWalletHistory,
                        false, LocalDateTime.now());
            }
            return new BaseResponse("Fail to delete order", eWalletHistory,
                    false, LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail in eWalletDeleteResponse", e.getMessage(),
                    false, LocalDateTime.now());
        }
    }
    private BookBuyingResponse convertBookListResponse(BookBuyable bookBuyable){
        return new BookBuyingResponse(bookBuyable.getBookName(), bookBuyable.getAuthorName(), bookBuyable.getPrice());
    }
}
