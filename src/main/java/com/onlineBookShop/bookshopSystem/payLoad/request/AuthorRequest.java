package com.onlineBookShop.bookshopSystem.payLoad.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorRequest {
    private String name;
    private LocalDate dob;
    private String address;
    private String email;
    private String phone;
}
