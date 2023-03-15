package com.onlineBookShop.bookshopSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.ValueOperations;

@SpringBootApplication
@EnableCaching
public class BookshopSystemApplication {
	public static void main(String[] args) {
		SpringApplication.run(BookshopSystemApplication.class, args);
	}
}
