package com.onlineBookShop.bookshopSystem.eWallet.controller;

import com.onlineBookShop.bookshopSystem.eWallet.entity.EWalletInfo;
import com.onlineBookShop.bookshopSystem.eWallet.service.EWalletInfoService;
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
    @RolesAllowed({"admin","user"})
    public BaseResponse getUserInfo(){
        return eWalletInfoService.getUserInfo();
    }

    @GetMapping("/all")
    @RolesAllowed("admin")
    public BaseResponse getAllUserInfo(){
        return eWalletInfoService.getAllUserInfo();
    }

    @PutMapping("/{accountName}")
    @RolesAllowed("admin")
    public BaseResponse updateUserBalanceInfo(@RequestBody BigDecimal balance,
                                       @PathVariable String accountName) {
        return eWalletInfoService.updateUserBalanceInfo(accountName,balance);
    }
    @PutMapping("")
    @RolesAllowed({"admin","user"})
    public BaseResponse updateUserNameInfo(@RequestBody String accountName){
        return eWalletInfoService.updateUserNameInfo(accountName);
    }
    @DeleteMapping("/{ownerId}")
    @RolesAllowed("admin")
    public BaseResponse deleteUserInfo(@PathVariable Long ownerId){
        return eWalletInfoService.deleteUserInfo(ownerId);
    }
}
