package com.onlineBookShop.bookshopSystem.service;

import com.onlineBookShop.bookshopSystem.entity.Author;
import com.onlineBookShop.bookshopSystem.payLoad.request.AuthorRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.AuthorResponse;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;

public interface AuthorService {
    Boolean checkAuthor(Long authorId);

    String getAuthorNameById(Long authorId);

    BaseResponse createAuthor(Author author);
    BaseResponse getAuthors(Integer pageNo, Integer pageSize, String sortBy);
    BaseResponse sortingAuthorList(Integer method);
    BaseResponse updateAuthor(String name, AuthorRequest authorRequest);
    BaseResponse deleteAuthor(String name);
    BaseResponse findAuthorByName(String name);
    Author getAuthorByName(String name);
    Long getAuthorIdByName(String name);
    AuthorResponse convertAuthorResponse(Author author);
}
