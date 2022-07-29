package com.onlineBookShop.bookshopSystem.payLoad.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllAuthorResponse {
    private List<AuthorResponse> authorList;
    private Integer pageNo;
    private Integer pageSize;
    private String sortBy;
}
