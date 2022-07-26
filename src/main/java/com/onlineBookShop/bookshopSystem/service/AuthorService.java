package com.onlineBookShop.bookshopSystem.service;

import com.onlineBookShop.bookshopSystem.entity.Author;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;

public interface AuthorService {
    Boolean checkAuthor(Long authorId);
    BaseResponse createAuthor(Author author);
    BaseResponse getAuthors(Integer pageNo, Integer pageSize, String sortBy);
    BaseResponse sortingAuthorList(Integer method);
    BaseResponse updateAuthor(Long id, Author author);
    BaseResponse deleteAuthor(Long id);
    BaseResponse getAuthorByID(long id);
}
