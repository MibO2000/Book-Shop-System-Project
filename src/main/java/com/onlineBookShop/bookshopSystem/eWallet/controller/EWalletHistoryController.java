package com.onlineBookShop.bookshopSystem.eWallet.controller;

import com.onlineBookShop.bookshopSystem.eWallet.service.EWalletHistoryService;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("ewallet/history")
public class EWalletHistoryController {
    private final EWalletHistoryService eWalletHistoryService;

    public EWalletHistoryController(EWalletHistoryService eWalletHistoryService) {
        this.eWalletHistoryService = eWalletHistoryService;
    }
    @GetMapping("")
    @RolesAllowed({"admin","user"})
    public BaseResponse getEachEWalletHistory(){
        return eWalletHistoryService.getEachEWalletHistory();
    }
    @GetMapping("/all")
    @RolesAllowed("admin")
    public BaseResponse getAllEWalletHistory(){
        return eWalletHistoryService.getAllEWalletHistory();
    }


}
