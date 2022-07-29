package com.onlineBookShop.bookshopSystem.eWallet.service;

import com.onlineBookShop.bookshopSystem.eWallet.entity.EWalletHistory;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;

import java.time.LocalDate;
import java.util.List;

public interface EWalletHistoryService {
    BaseResponse getAllEWalletHistory();
    BaseResponse getEachEWalletHistory();
    Boolean deleteHistory(Long orderId);
    Boolean createHistory(EWalletHistory eWalletHistory);
    List<EWalletHistory> getHistoryByDate(LocalDate date);
    EWalletHistory getHistoryByBookID(Long orderId);
    BaseResponse getSpecificEWalletHistory(Long id);

}
