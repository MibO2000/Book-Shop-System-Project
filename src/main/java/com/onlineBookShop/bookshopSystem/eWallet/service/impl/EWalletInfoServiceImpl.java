package com.onlineBookShop.bookshopSystem.eWallet.service.impl;

import com.onlineBookShop.bookshopSystem.config.AppConfig;
import com.onlineBookShop.bookshopSystem.eWallet.entity.EWalletInfo;
import com.onlineBookShop.bookshopSystem.eWallet.mapper.EWalletInfoMapper;
import com.onlineBookShop.bookshopSystem.eWallet.service.EWalletInfoService;
import com.onlineBookShop.bookshopSystem.payLoad.request.BalanceUpdateRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.payLoad.response.EWalletInfoResponse;
import com.onlineBookShop.bookshopSystem.repository.UserRepository;
import com.onlineBookShop.bookshopSystem.service.KeycloakService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    public EWalletInfoServiceImpl(AppConfig appConfig, EWalletInfoMapper mapper,
                                  UserRepository userRepository, KeycloakService keycloakService) {
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
            EWalletInfo info = jdbcTemplate.queryForObject(appConfig.getEWalletInfo().getInfoQuery(),new Object[]{id},mapper);
            return new BaseResponse("Here is the data",
                    new EWalletInfoResponse(info.getAccountName(),info.getBalance()),
                    true,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to get User's wallet info",e.getMessage(),
                    false, LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse getAllUserInfo() {
        try{
            List<EWalletInfo> userInfo = jdbcTemplate.query(appConfig.getEWalletInfo().getInfoAllQuery(),mapper);
            return new BaseResponse("Here is all user wallet list",
                    userInfo.stream().map(this::convertEWalletInfoResponse),
                    true,LocalDateTime.now());
        }catch(Exception e){
            return new BaseResponse("Fail to get aLl user wallet list",e.getMessage(),
                    false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse updateUserBalanceInfo(BalanceUpdateRequest balanceUpdateRequest) {
        String accountName = balanceUpdateRequest.getName();
        BigDecimal balance = balanceUpdateRequest.getBalance();
        if (accountName == null || balance == null){
            return new BaseResponse("Need to fill info",null,false,LocalDateTime.now());
        }
        try{
            if(jdbcTemplate.update(appConfig.getEWalletInfo().getInfoUpdateQuery(),balance,accountName)>0){
                return new BaseResponse("The user info updated",
                                        new EWalletInfoResponse(accountName,balance),
                                        true,LocalDateTime.now());
            }
            return new BaseResponse("The user info not updated",
                                    new EWalletInfoResponse(accountName,balance),
                                    false,LocalDateTime.now());
        }catch(Exception e){
            return new BaseResponse("Fail to update user",e.getMessage(),false,LocalDateTime.now());
        }
    }

    @Override
    public Boolean deleteUserInfo(Long ownerId) {
        if (ownerId == null){
            return false;
        }
        EWalletInfo eWalletInfo = jdbcTemplate.queryForObject(appConfig.getEWalletInfo().getInfoQuery(),
                                                                new Object[]{ownerId},mapper);
        if (eWalletInfo==null){
            return false;
        }
        try{
            jdbcTemplate.update(appConfig.getEWalletInfo().getInfoDeleteQuery(),ownerId);
            return true;
        }catch(Exception e){
            return true;
        }
    }

    @Override
    public Boolean updateBalanceAfterBuying(BigDecimal balance) {
        Long id = getUserId();
        try{
            if(jdbcTemplate.update(appConfig.getEWalletInfo().getUpdateBalanceQuery(),balance,id)>0){
                return true;
            }
            return false;
        }catch (Exception e){
            log.error("Error: "+e);
            return false;
        }
    }
    @Override
    public Boolean updateBalanceAfterDeleting(BigDecimal balance){
        Long id = getUserId();
        try{
            if(jdbcTemplate.update(appConfig.getEWalletInfo().getAddBalanceQuery(),balance,id)>0){
                return true;
            }
            return false;
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
    private EWalletInfoResponse convertEWalletInfoResponse(EWalletInfo eWalletInfo){
        return new EWalletInfoResponse(eWalletInfo.getAccountName(),eWalletInfo.getBalance());
    }

}
