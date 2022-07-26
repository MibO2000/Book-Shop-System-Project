package com.onlineBookShop.bookshopSystem.service.impl;

import com.onlineBookShop.bookshopSystem.eWallet.entity.EWalletInfo;
import com.onlineBookShop.bookshopSystem.eWallet.service.EWalletInfoService;
import com.onlineBookShop.bookshopSystem.entity.User;
import com.onlineBookShop.bookshopSystem.payLoad.request.UserCreationRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.repository.UserRepository;
import com.onlineBookShop.bookshopSystem.service.KeycloakService;
import com.onlineBookShop.bookshopSystem.service.UserService;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EWalletInfoService eWalletInfoService;
    private final KeycloakService keycloakService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           EWalletInfoService eWalletInfoService, KeycloakService keycloakService) {
        this.userRepository = userRepository;
        this.eWalletInfoService = eWalletInfoService;
        this.keycloakService = keycloakService;
    }

    @Override
    public BaseResponse getAllUsers(Integer pageNo, Integer pageSize, String sortBy) {
        try{
            Pageable pageable = PageRequest.of(pageNo-1,pageSize,Sort.by(sortBy));
            Page<User> pagedResult = userRepository.findAll(pageable);
            return new BaseResponse("Here is the list of users",pagedResult,true,LocalDateTime.now());
        }catch(Exception e){
            return new BaseResponse("Fail to get all the users",null,false, LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse createNewUser(UserCreationRequest userCreationRequest) {
        if (userCreationRequest == null){
            return new BaseResponse("No Input found",null,false,LocalDateTime.now());
        }
        User user = userCreationRequest.getUser();
        BigDecimal balance = userCreationRequest.getBalance();
        try{
            UserRepresentation kcuser = keycloakService.createUser(user);
            if (kcuser != null){
                User newUser = userRepository.save(new User(user.getName(),user.getDob(),user.getAddress(),
                                                            user.getEmail(),user.getPhone(),LocalDateTime.now(),
                                                            kcuser.getId()));
                EWalletInfo eWalletInfo = new EWalletInfo(newUser.getId(), newUser.getName(), LocalDateTime.now(), balance);
                if(eWalletInfoService.createNewEWallet(eWalletInfo)){
                    return new BaseResponse("New User Created",newUser,true,LocalDateTime.now());
                }
                return new BaseResponse("Fail to create Wallet",newUser,false,LocalDateTime.now());
            }
            else{
                return new BaseResponse("Fail to create data in Keycloak",user,false,LocalDateTime.now());
            }
        }catch (Exception e){
            return new BaseResponse("Fail",null,false,LocalDateTime.now());
        }
    }

    @Override
    public Long getUserId() {
        try{
            User user = userRepository.findByKeycloakId(keycloakService.getUserKeycloakId());
            return user.getId();
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public User getUserById(Long userId) {
        try {
            return userRepository.findUserById(userId);
        }catch (Exception e){
            return null;
        }

    }
}
