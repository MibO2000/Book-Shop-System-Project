package com.onlineBookShop.bookshopSystem.service.impl;

import com.onlineBookShop.bookshopSystem.config.AppConfig;
import com.onlineBookShop.bookshopSystem.eWallet.entity.EWalletHistory;
import com.onlineBookShop.bookshopSystem.eWallet.entity.EWalletInfo;
import com.onlineBookShop.bookshopSystem.eWallet.mapper.EWalletHistoryMapper;
import com.onlineBookShop.bookshopSystem.eWallet.service.EWalletInfoService;
import com.onlineBookShop.bookshopSystem.entity.User;
import com.onlineBookShop.bookshopSystem.payLoad.request.UserCreationRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.AllUserResponse;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.payLoad.response.UserResponse;
import com.onlineBookShop.bookshopSystem.repository.UserRepository;
import com.onlineBookShop.bookshopSystem.service.KeycloakService;
import com.onlineBookShop.bookshopSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final EWalletHistoryMapper eWalletHistoryMapper;
    private final AppConfig appConfig;
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final EWalletInfoService eWalletInfoService;
    private final KeycloakService keycloakService;

    @Autowired
    public UserServiceImpl(EWalletHistoryMapper eWalletHistoryMapper, AppConfig appConfig,
                           JdbcTemplate jdbcTemplate, UserRepository userRepository,
                           EWalletInfoService eWalletInfoService, KeycloakService keycloakService) {
        this.eWalletHistoryMapper = eWalletHistoryMapper;
        this.appConfig = appConfig;
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.eWalletInfoService = eWalletInfoService;
        this.keycloakService = keycloakService;
    }

    @Override
    public BaseResponse getAllUsers(Integer pageNo, Integer pageSize, String sortBy) {
        try{
            Pageable pageable = PageRequest.of(pageNo-1,pageSize,Sort.by(sortBy));
            Page<User> pagedResult = userRepository.findAll(pageable);
            AllUserResponse response = new AllUserResponse(
                    pagedResult.stream().map(this::convertUserResponse).toList(),pageNo,pageSize,sortBy
            );
            return new BaseResponse("Here is the list of users",response,
                    true,LocalDateTime.now());
        }catch(Exception e){
            return new BaseResponse("Fail to get all the users",e.getMessage(),
                    false, LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse createNewUser(UserCreationRequest userCreationRequest) {
        if (userCreationRequest == null){
            return new BaseResponse("No Input found",null,false,LocalDateTime.now());
        }
        User user = userCreationRequest.getUser();
        BigDecimal balance = userCreationRequest.getBalance();
        if(!userCheck(user)){
            return new BaseResponse("Duplicate Available",convertUserResponse(user),
                    false,LocalDateTime.now());
        }
        try{
            UserRepresentation kcuser = keycloakService.createUser(user);
            if (kcuser !=  null){
                User newUser = userRepository.save(new User(user.getName(),user.getDob(),user.getAddress(),
                                                            user.getEmail(),user.getPhone(),LocalDateTime.now(),
                                                            kcuser.getId()));
                EWalletInfo eWalletInfo = new EWalletInfo
                        (newUser.getId(), newUser.getName(), LocalDateTime.now(), balance);
                if(eWalletInfoService.createNewEWallet(eWalletInfo)){
                    return new BaseResponse("New User Created",convertUserResponse(newUser),
                            true,LocalDateTime.now());
                }
                return new BaseResponse("Fail to create Wallet",convertUserResponse(newUser),
                        false,LocalDateTime.now());
            }
            else{
                return new BaseResponse("Fail to create data in Keycloak",convertUserResponse(user),
                        false,LocalDateTime.now());
            }
        }catch (Exception e){
            return new BaseResponse("Fail",e.getMessage(),false,LocalDateTime.now());
        }
    }

    private Boolean userCheck(User user) {
        try{
            String name = user.getName();
            String email = user.getEmail();
            User user1 = userRepository.findUserByName(name);
            User user2 = userRepository.findUserByEmail(email);
            return user1 == null && user2 == null;
        }catch (Exception e){
            log.error("Error: {}",e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public Long getUserId() {
        try{
            User user = userRepository.findByKeycloakId(keycloakService.getUserKeycloakId());
            return user.getId();
        }catch (Exception e){
            log.error("Error: {}",e.getMessage());
            return null;
        }
    }

    @Override
    public User getUserById(Long userId) {
        try {
            return userRepository.findUserById(userId);
        }catch (Exception e){
            log.error("Error: {}",e.getMessage());
            return null;
        }

    }

    @Override
    public BaseResponse findUserByName(String name) {
        try{
            User user = userRepository.findUserByName(name);
            if (user == null){
                return new BaseResponse("User "+name+" does not exist",null,
                                        false,LocalDateTime.now());
            }
            return new BaseResponse("Here is the user",convertUserResponse(user),
                    true,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to get user",e.getMessage(),false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse deleteUser(String name) {
        try{
            User userToDelete = userRepository.findUserByName(name);
            if (userToDelete == null){
                return new BaseResponse("User not found",null,false,LocalDateTime.now());
            }
            long userId = userToDelete.getId();
            List<EWalletHistory> eWalletHistoryList = checkHistoryToDeleteUser(userId);
            if (!eWalletHistoryList.isEmpty()){
                return new BaseResponse("Cannot delete user yet as books are left to deliver",
                        eWalletHistoryList,false,LocalDateTime.now());
            }
            UserRepresentation userRepresentation = keycloakService.deleteUser(userToDelete);
            if (userRepresentation != null){
                userRepository.delete(userToDelete);
                if (eWalletInfoService.deleteUserInfo(userId)){
                    return new BaseResponse("User Deleted",convertUserResponse(userToDelete),
                            true,LocalDateTime.now());
                }
                return new BaseResponse("Fail to delete eWalletInfo",convertUserResponse(userToDelete),
                        false,LocalDateTime.now());
            }
            return new BaseResponse("Fail to delete keycloak user info",convertUserResponse(userToDelete),
                    false,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to delete user",e.getMessage(),false,LocalDateTime.now());
        }
    }
    public List<EWalletHistory> checkHistoryToDeleteUser(Long ownerId) {
        try{
            LocalDate date1 = LocalDate.now();
            LocalDate date2 = LocalDate.now().minusDays(1);
            LocalDate date3 = LocalDate.now().minusDays(2);
            return jdbcTemplate.query(appConfig.getEWalletHistory().getHistoryQueryByOwnerIdToDelete(),
                    new Object[]{ownerId,date1,date2,date3},eWalletHistoryMapper);
        }catch (Exception e){
            log.error("Error: {}",e.getMessage());
            return null;
        }
    }
    private UserResponse convertUserResponse(User user){
        return new UserResponse(user.getName(),user.getDob(),user.getAddress(),user.getEmail(),user.getPhone());
    }
}
