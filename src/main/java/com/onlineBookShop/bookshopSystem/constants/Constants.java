package com.onlineBookShop.bookshopSystem.constants;

public class Constants {
    //book repo
    public static final String SORTING_BOOKS_ASC = "SELECT * FROM Book AS b ORDER BY b.id ASC";
    public static final String SORTING_BOOKS_DESC = "SELECT * FROM Book AS b ORDER BY b.id DESC";
    public static final String ID_FOR_DELETE = "SELECT b.id FROM Book AS b, Author AS a WHERE b.author_id = a.id AND a.id=?1";
    public static final String FIND_BOOK_AVAILABILITY = "SELECT book_count FROM Book AS b WHERE b.book_count>0 AND b.id = ?1 AND b.author_id=?2";
    public static final String GET_PRICE = "SELECT b.price FROM Book AS b WHERE b.id = ?1";

    //author repo
    public static final String SORTING_AUTHORS_ASC = "SELECT * FROM Author AS a ORDER BY a.id ASC";
    public static final String SORTING_AUTHORS_DESC = "SELECT * FROM Author AS a ORDER BY a.id DESC";


}
