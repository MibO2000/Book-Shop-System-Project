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
    @RolesAllowed("admin")
    public BaseResponse getAllUsers(@RequestParam(defaultValue = "1") Integer pageNo,
                                    @RequestParam(defaultValue = "10") Integer pageSize,
                                    @RequestParam(defaultValue = "id") String sortBy){
        return userService.getAllUsers(pageNo,pageSize,sortBy);
    }
    @GetMapping("/{id}")
    @RolesAllowed("admin")
    public BaseResponse getUserById(@PathVariable(required = false)Long id){
        try{
            return new BaseResponse("Here is the user with id: "+id,userService.getUserById(id),true,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to get User with id:"+id,null,false, LocalDateTime.now());
        }

    }
    @PostMapping()
    @RolesAllowed("admin")
    public BaseResponse createNewUser(@RequestBody UserCreationRequest userCreationRequest){
        return userService.createNewUser(userCreationRequest);
    }
}
