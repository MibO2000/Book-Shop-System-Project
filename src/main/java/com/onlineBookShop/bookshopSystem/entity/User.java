package com.onlineBookShop.bookshopSystem.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column
    private String name;
    @Column
    private LocalDate dob;
    @Column
    private String address;
    @Column
    private String email;
    @Column
    private String phone;
    @Column
    private LocalDateTime createdAt;
    @Column
    private String keycloakId;

    public User(String name, LocalDate dob, String address, String email,
                String phone, LocalDateTime createdAt, String keycloakId) {
        this.name = name;
        this.dob = dob;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.createdAt = createdAt;
        this.keycloakId = keycloakId;
    }
}
