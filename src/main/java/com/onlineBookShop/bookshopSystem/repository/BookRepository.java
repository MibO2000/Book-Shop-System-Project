package com.onlineBookShop.bookshopSystem.repository;

import com.onlineBookShop.bookshopSystem.constants.Constants;
import com.onlineBookShop.bookshopSystem.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Query(value = Constants.SORTING_BOOKS_ASC, nativeQuery = true)
    List<Book> ascendingBookSorting();
    @Query(value = Constants.SORTING_BOOKS_DESC, nativeQuery = true)
    List<Book> descendingBookSorting();
    @Query(value = Constants.ID_FOR_DELETE,nativeQuery = true)
    List<Long> idsForDelete(long id);
    List<Book> findBooksByBookCountGreaterThan(Integer count);
    @Query(value = Constants.FIND_BOOK_AVAILABILITY, nativeQuery = true)
    Integer findBookAvailability(Long bookId, Long authorId);
    @Query(value = Constants.GET_PRICE,nativeQuery = true)
    BigDecimal getBookPrice(Long id);
    Book findBookById(Long id);
    Book findBookByName(String name);
    List<Book> findBooksByAuthorId(Long id);
}
