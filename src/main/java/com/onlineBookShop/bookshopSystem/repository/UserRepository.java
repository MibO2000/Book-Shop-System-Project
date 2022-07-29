package com.onlineBookShop.bookshopSystem.repository;

import com.onlineBookShop.bookshopSystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
    User findByKeycloakId(String KeyCloakId);
    User findUserById(Long userId);
    User findUserByName(String name);
    User findUserByEmail(String email);
}
