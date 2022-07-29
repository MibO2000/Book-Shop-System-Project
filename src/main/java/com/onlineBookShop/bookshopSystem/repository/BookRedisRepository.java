package com.onlineBookShop.bookshopSystem.repository;

import com.onlineBookShop.bookshopSystem.entity.BookBuyable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRedisRepository extends JpaRepository<BookBuyable, Long> {
}
