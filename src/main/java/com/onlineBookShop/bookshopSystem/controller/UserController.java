package com.onlineBookShop.bookshopSystem.controller;

import com.onlineBookShop.bookshopSystem.payLoad.request.UserCreationRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("")
    @CrossOrigin
    @RolesAllowed("admin")
    public BaseResponse getAllUsers(@RequestParam(defaultValue = "1") Integer pageNo,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(defaultValue = "id") String sortBy){
        return userService.getAllUsers(pageNo,pageSize,sortBy);
    }
    @GetMapping("/")
    @CrossOrigin
    @RolesAllowed("admin")
    public BaseResponse getUserById(@RequestParam String name){
        return userService.findUserByName(name);
    }
    @PostMapping()
    @CrossOrigin
    @RolesAllowed("admin")
    public BaseResponse createNewUser(@RequestBody UserCreationRequest userCreationRequest){
        return userService.createNewUser(userCreationRequest);
    }
    @DeleteMapping("/delete/")
    @CrossOrigin
    @RolesAllowed("admin")
    public BaseResponse deleteUser(@RequestParam String name){
        return userService.deleteUser(name);
    }
}
