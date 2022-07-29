package com.onlineBookShop.bookshopSystem.service;

import com.onlineBookShop.bookshopSystem.entity.Author;
import com.onlineBookShop.bookshopSystem.payLoad.response.AuthorResponse;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;

public interface AuthorService {
    Boolean checkAuthor(Long authorId);

    String getAuthorNameById(Long authorId);

    BaseResponse createAuthor(Author author);
    BaseResponse getAuthors(Integer pageNo, Integer pageSize, String sortBy);
    BaseResponse sortingAuthorList(Integer method);
    BaseResponse updateAuthor(String name, Author author);
    BaseResponse deleteAuthor(Long id);
    BaseResponse findAuthorByName(String name);
    Author getAuthorByName(String name);
    Author findById(long authorId);
    Long getAuthorIdByName(String name);
    AuthorResponse convertAuthorResponse(Author author);
}
