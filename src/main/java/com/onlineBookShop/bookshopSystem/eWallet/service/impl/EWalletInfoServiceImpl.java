package com.onlineBookShop.bookshopSystem.eWallet.service.impl;

import com.onlineBookShop.bookshopSystem.config.AppConfig;
import com.onlineBookShop.bookshopSystem.eWallet.entity.EWalletInfo;
import com.onlineBookShop.bookshopSystem.eWallet.mapper.EWalletInfoMapper;
import com.onlineBookShop.bookshopSystem.eWallet.service.EWalletInfoService;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.repository.UserRepository;
import com.onlineBookShop.bookshopSystem.service.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class EWalletInfoServiceImpl implements EWalletInfoService {
    private final AppConfig appConfig;
    private final JdbcTemplate jdbcTemplate;
    private final EWalletInfoMapper mapper;
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;
    @Autowired
    public EWalletInfoServiceImpl(AppConfig appConfig, EWalletInfoMapper mapper, UserRepository userRepository, KeycloakService keycloakService) {
        this.appConfig = appConfig;
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.keycloakService = keycloakService;
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(appConfig.getEWalletDatasource().getSourceUrl());
        dataSource.setDriverClassName(appConfig.getEWalletDatasource().getDriverClassname());
        dataSource.setUsername(appConfig.getEWalletDatasource().getUsername());
        dataSource.setPassword(appConfig.getEWalletDatasource().getPassword());
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public boolean createNewEWallet(EWalletInfo eWalletInfo) {
        try{
            jdbcTemplate.update(appConfig.getEWalletInfo().getInfoInsertQuery(), eWalletInfo.getOwnerId(),
                    eWalletInfo.getAccountName(),eWalletInfo.getBalance(),eWalletInfo.getCreateAt());
            return true;
        }catch (Exception e){
            log.error("Fail to create Wallet, exception: " + e);
            return false;
        }
    }
    public Long getUserId(){
        try{
            return userRepository.findByKeycloakId(keycloakService.getUserKeycloakId()).getId();
        }catch (Exception e){
            log.error("Error: "+e);
            return null;
        }
    }

    @Override
    public BaseResponse getUserInfo() {
        try{
            Long id = getUserId();
            return new BaseResponse("Here is the data",
                    jdbcTemplate.queryForObject(appConfig.getEWalletInfo().getInfoQuery(),new Object[]{id},mapper),
                    true,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to get User's wallet info",null,false, LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse getAllUserInfo() {
        try{
            return new BaseResponse("Here is all user wallet list",
                    jdbcTemplate.query(appConfig.getEWalletInfo().getInfoAllQuery(),mapper),
                    true,LocalDateTime.now());
        }catch(Exception e){
            return new BaseResponse("Fail to get aLl user wallet list",null,false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse updateUserBalanceInfo(String accountName, BigDecimal balance) {
        if (accountName == null || balance == null){
            return new BaseResponse("Need to fill info",null,false,LocalDateTime.now());
        }
        if (!getUserInfo().isStatus()){
            return new BaseResponse("The user does not exists",null,false,LocalDateTime.now());
        }
        try{
            jdbcTemplate.update(appConfig.getEWalletInfo().getInfoUpdateQuery(),balance,accountName);
            return new BaseResponse("The user info updated",getUserInfo(),true,LocalDateTime.now());
        }catch(Exception e){
            return new BaseResponse("Fail to update user",null,false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse deleteUserInfo(Long ownerId) {
        if (ownerId == null){
            return new BaseResponse("Need to fill info",null,false,LocalDateTime.now());
        }
        EWalletInfo eWalletInfo = jdbcTemplate.queryForObject(appConfig.getEWalletInfo().getInfoQuery(),new Object[]{ownerId},mapper);
        if (eWalletInfo==null){
            return new BaseResponse("The user does not exists",null,false,LocalDateTime.now());
        }
        try{
            jdbcTemplate.update(appConfig.getEWalletInfo().getInfoDeleteQuery(),ownerId);
            return new BaseResponse("User info deleted",eWalletInfo,true,LocalDateTime.now());
        }catch(Exception e){
            return new BaseResponse("Fail to delete user info",null,false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse updateUserNameInfo(String accountName) {
        if (accountName == null){
            return new BaseResponse("Need to fill info",null,false,LocalDateTime.now());
        }
        Long id = getUserId();
        try{
            jdbcTemplate.update(appConfig.getEWalletInfo().getUpdateNameQuery(),accountName,id);
            return new BaseResponse("Account name updated",getUserInfo().getResult(),true,LocalDateTime.now());
        }catch (Exception e){
            log.error("Error: "+e);
        }
        return null;
    }

    @Override
    public Boolean updateBalanceAfterBuying(BigDecimal balance) {
        Long id = getUserId();
        try{
            jdbcTemplate.update(appConfig.getEWalletInfo().getUpdateBalanceQuery(),balance,id);
            return true;
        }catch (Exception e){
            log.error("Error: "+e);
            return false;
        }
    }
    @Override
    public Boolean updateBalanceAfterDeleting(BigDecimal balance){
        Long id = getUserId();
        try{
            jdbcTemplate.update(appConfig.getEWalletInfo().getAddBalanceQuery(),balance,id);
            return true;
        }catch (Exception e){
            log.error("Error: "+e);
            return false;
        }
    }

    @Override
    public BigDecimal getBalance() {
        Long ownerId = getUserId();
        try{
            return Objects.requireNonNull(jdbcTemplate.queryForObject(appConfig.getEWalletInfo().getInfoQuery(),
                    new Object[]{ownerId}, mapper)).getBalance();
        }catch (Exception e){
            log.error("Error: "+e);
            return null;
        }
    }

}
