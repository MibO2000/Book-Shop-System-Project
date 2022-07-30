package com.onlineBookShop.bookshopSystem.controller;

import com.onlineBookShop.bookshopSystem.entity.Author;
import com.onlineBookShop.bookshopSystem.payLoad.request.AuthorRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/author")
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("")
    @CrossOrigin
    @RolesAllowed({"user","admin"})
    public BaseResponse getAuthors(@RequestParam(defaultValue = "1")Integer pageNo,
                                   @RequestParam(defaultValue = "10")Integer pageSize,
                                   @RequestParam(defaultValue = "name")String sortBy){
        return authorService.getAuthors(pageNo,pageSize,sortBy);
    }
    @GetMapping("/name/")
    @CrossOrigin
    @RolesAllowed({"user","admin"})
    public BaseResponse getAuthorByName(@RequestParam String name){
        return authorService.findAuthorByName(name);
    }

    @PostMapping("")
    @CrossOrigin
    @RolesAllowed("admin")
    public BaseResponse createAuthor(@RequestBody Author author){
        return authorService.createAuthor(author);
    }

    @GetMapping("/method")
    @CrossOrigin
    @RolesAllowed({"user","admin"})
    public BaseResponse sortingAuthorList(@RequestParam(name = "method",required = false) Integer method){
        return authorService.sortingAuthorList(method);
    }

    @PutMapping("/")
    @CrossOrigin
    @RolesAllowed("admin")
    public BaseResponse updateAuthor(@RequestParam String name, @RequestBody AuthorRequest authorRequest){
        return authorService.updateAuthor(name,authorRequest);
    }

    @DeleteMapping("/delete/")
    @CrossOrigin
    @RolesAllowed("admin")
    public BaseResponse deleteAuthor(@RequestParam String name){ return authorService.deleteAuthor(name); }
}
