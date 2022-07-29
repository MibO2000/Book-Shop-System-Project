package com.onlineBookShop.bookshopSystem.controller;

import com.onlineBookShop.bookshopSystem.payLoad.request.UserCreationRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.time.LocalDateTime;

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
    @GetMapping("/{id}")
    @CrossOrigin
    @RolesAllowed("admin")
    public BaseResponse getUserById(@PathVariable Long id){
        return userService.findUserById(id);
    }
    @PostMapping()
    @CrossOrigin
    @RolesAllowed("admin")
    public BaseResponse createNewUser(@RequestBody UserCreationRequest userCreationRequest){
        return userService.createNewUser(userCreationRequest);
    }
    @DeleteMapping("/{userId}")
    @CrossOrigin
    @RolesAllowed("admin")
    public BaseResponse deleteUser(@PathVariable Long userId){
        return userService.deleteUser(userId);
    }
}
