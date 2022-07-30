package com.onlineBookShop.bookshopSystem.service;

import com.onlineBookShop.bookshopSystem.entity.User;
import com.onlineBookShop.bookshopSystem.payLoad.request.UserCreationRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;

public interface UserService {
    BaseResponse getAllUsers(Integer pageNo, Integer pageSize, String sortBy);
    BaseResponse createNewUser(UserCreationRequest userCreationRequest);
    Long getUserId();
    User getUserById(Long userId);
    BaseResponse findUserByName(String name);
    BaseResponse deleteUser(String name);
}
