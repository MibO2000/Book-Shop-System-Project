package com.onlineBookShop.bookshopSystem.repository;

import com.onlineBookShop.bookshopSystem.constants.Constants;
import com.onlineBookShop.bookshopSystem.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query(value = Constants.SORTING_AUTHORS_ASC, nativeQuery = true)
    List<Author> ascendingAuthorSorting();
    @Query(value = Constants.SORTING_AUTHORS_DESC, nativeQuery = true)
    List<Author> descendingAuthorSorting();
    Author findAuthorById(Long id);
    Author findByName(String name);
    Author findAuthorByName(String name);
    @Query(value = Constants.GET_ID_BY_AUTHOR_NAME,nativeQuery = true)
    Long getAuthorIdByName(String name);
}
