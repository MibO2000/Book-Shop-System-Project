package com.onlineBookShop.bookshopSystem.eWallet.controller;

import com.onlineBookShop.bookshopSystem.eWallet.entity.EWalletInfo;
import com.onlineBookShop.bookshopSystem.eWallet.service.EWalletInfoService;
import com.onlineBookShop.bookshopSystem.payLoad.request.BalanceUpdateRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.math.BigDecimal;

@RestController
@RequestMapping("ewallet/info")
public class EWalletInfoController {
    private final EWalletInfoService eWalletInfoService;

    @Autowired
    public EWalletInfoController(EWalletInfoService eWalletInfoService) {
        this.eWalletInfoService = eWalletInfoService;
    }

    @PostMapping()
    @RolesAllowed("admin")
    public boolean createNewEWallet(EWalletInfo eWalletInfo){
        return eWalletInfoService.createNewEWallet(eWalletInfo);
    }
    @GetMapping()
    @RolesAllowed("user")
    public BaseResponse getUserInfo(){
        return eWalletInfoService.getUserInfo();
    }

    @GetMapping("/all")
    @RolesAllowed("admin")
    public BaseResponse getAllUserInfo(){
        return eWalletInfoService.getAllUserInfo();
    }

    @PutMapping("/balance/update")
    @RolesAllowed("admin")
    public BaseResponse updateUserBalanceInfo(@RequestBody BalanceUpdateRequest balanceUpdateRequest) {
        return eWalletInfoService.updateUserBalanceInfo(balanceUpdateRequest);
    }
    @PutMapping("")
    @RolesAllowed("user")
    public BaseResponse updateUserNameInfo(@RequestBody String accountName){
        return eWalletInfoService.updateUserNameInfo(accountName);
    }
}
