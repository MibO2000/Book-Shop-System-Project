package com.onlineBookShop.bookshopSystem.eWallet.controller;

import com.onlineBookShop.bookshopSystem.eWallet.service.EWalletHistoryService;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("ewallet/history")
public class EWalletHistoryController {
    private final EWalletHistoryService eWalletHistoryService;

    @Autowired
    public EWalletHistoryController(EWalletHistoryService eWalletHistoryService) {
        this.eWalletHistoryService = eWalletHistoryService;
    }
    @GetMapping("")
    @CrossOrigin
    @RolesAllowed("user")
    public BaseResponse getEachEWalletHistory(){
        return eWalletHistoryService.getEachEWalletHistory();
    }

    @GetMapping("/all")
    @CrossOrigin
    @RolesAllowed("admin")
    public BaseResponse getAllEWalletHistory(){
        return eWalletHistoryService.getAllEWalletHistory();
    }
    @GetMapping("/{id}")
    @CrossOrigin
    @RolesAllowed("admin")
    public BaseResponse getSpecificEWalletHistory(@PathVariable Long id){
        return eWalletHistoryService.getSpecificEWalletHistory(id);
    }
}
