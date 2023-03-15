package com.onlineBookShop.bookshopSystem.payLoad.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllUserResponse {
    private List<UserResponse> userList;
    private Integer pageNo;
    private Integer pageSize;
    private String sortBy;
}
