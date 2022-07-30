package com.onlineBookShop.bookshopSystem.eWallet.service;

import com.onlineBookShop.bookshopSystem.eWallet.entity.EWalletInfo;
import com.onlineBookShop.bookshopSystem.payLoad.request.BalanceUpdateRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;

import java.math.BigDecimal;

public interface EWalletInfoService {
    boolean createNewEWallet(EWalletInfo eWalletInfo);
    BaseResponse getUserInfo();
    BaseResponse getAllUserInfo();
    BaseResponse updateUserBalanceInfo(BalanceUpdateRequest balanceUpdateRequest);
    Boolean updateBalanceAfterBuying(BigDecimal balance);
    Boolean updateBalanceAfterDeleting(BigDecimal balance);
    BigDecimal getBalance();
    Boolean deleteUserInfo(Long userId);
}
