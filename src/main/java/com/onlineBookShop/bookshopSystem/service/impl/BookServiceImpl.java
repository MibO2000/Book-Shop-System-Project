package com.onlineBookShop.bookshopSystem.service.impl;

import com.onlineBookShop.bookshopSystem.entity.Author;
import com.onlineBookShop.bookshopSystem.entity.Book;
import com.onlineBookShop.bookshopSystem.payLoad.request.BookCreateRequest;
import com.onlineBookShop.bookshopSystem.payLoad.response.AllBookResponse;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.payLoad.response.BookResponse;
import com.onlineBookShop.bookshopSystem.repository.BookRepository;
import com.onlineBookShop.bookshopSystem.service.AuthorService;
import com.onlineBookShop.bookshopSystem.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorService authorService;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, AuthorService authorService) {
        this.bookRepository = bookRepository;
        this.authorService = authorService;
    }

    @Override
    public BaseResponse findAllBook(Integer pageNo, Integer pageSize, String sortBy) {
        try {
            Pageable pageable = PageRequest.of(pageNo-1, pageSize, Sort.by(sortBy));
            Page<Book> resultPage = bookRepository.findAll(pageable);
            AllBookResponse response = new AllBookResponse(resultPage.stream().map(this::convertBookResponse).toList(),
                                                            pageNo, pageSize,sortBy);
            return new BaseResponse("Here is all books",response, true, LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to find all books",e.getMessage(),false, LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse createBook(BookCreateRequest request) {
        try{
            Long authorId = authorService.getAuthorIdByName(request.getAuthorName());
            if (!authorService.checkAuthor(authorId)){
                return new BaseResponse("Create the author first!",null,
                        false, LocalDateTime.now());
            }
            Book book= bookRepository.findBookByName(request.getBookName());
            if(book != null){
                return new BaseResponse("Duplicate name", null, false, LocalDateTime.now());

            }
            Book uploadedBook = bookRepository.save(new Book(request.getBookName(),authorId,
                    request.getDateOfRelease(),request.getPrice(), request.getBookCount(), LocalDateTime.now()));
            return new BaseResponse("New Book created", convertBookResponse(uploadedBook),
                    true,LocalDateTime.now());
        } catch (Exception e){
            return new BaseResponse("Fail to upload book",e.getMessage(),false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse getBooksByAuthorName(String name) {
        try{
            Author author = authorService.getAuthorByName(name);
            if (author == null){
                return new BaseResponse("Author not exists",null,false,LocalDateTime.now());
            }
            List<Book> bookList = bookRepository.findBooksByAuthorId(author.getId());
            if (bookList.isEmpty()){
                return new BaseResponse("The author has no book", null, true,LocalDateTime.now());
            }
            return new BaseResponse("Here is the list",bookList.stream().map(this::convertBookResponse),
                    true,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to get books by author id",e.getMessage(),
                    false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse sortingBookList(Integer method) {
        try{
            return switch (method) {
                case 1 ->
                        new BaseResponse("Here is the ascending sorted list",
                                bookRepository.ascendingBookSorting().stream().map(this::convertBookResponse),
                                true, LocalDateTime.now());
                case 2 ->
                        new BaseResponse("Here is the descending sorted list",
                                bookRepository.descendingBookSorting().stream().map(this::convertBookResponse),
                                true, LocalDateTime.now());
                default -> new BaseResponse("Incorrect Input", null, false, LocalDateTime.now());
            };
        }catch (Exception e){
            return new BaseResponse("Fail to sort the book list",e.getMessage(),
                    false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse updateBook(String name, BookCreateRequest request) {
        try{
            Book bookToUpdate = bookRepository.findBookByName(name);
            if(bookToUpdate != null){
                bookToUpdate.setName(request.getBookName());
                bookToUpdate.setPrice(request.getPrice());
                bookToUpdate.setBookCount(request.getBookCount());
                bookToUpdate.setDateOfRelease(request.getDateOfRelease());
                bookToUpdate.setAuthorId(authorService.getAuthorIdByName(request.getAuthorName()));
                bookRepository.save(bookToUpdate);
                return new BaseResponse("Book "+ name + " is updated", convertBookResponse(bookToUpdate),
                        true,LocalDateTime.now());
            }
            return new BaseResponse("Book "+name+" is not present",null,false,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to updaate a book with name:"+name,e.getMessage(),
                    false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse deleteBook(String name) {
        try{
            Book bookToDelete = findBookByName(name);
            if (bookToDelete == null){
                return new BaseResponse("Book: " + name+" is not in the list", null,
                        false,LocalDateTime.now());
            }
            long id = bookToDelete.getId();
            Optional<Book> checkBook = bookRepository.findById(id);
            if (checkBook.isPresent()){
                Book book = bookRepository.getReferenceById(id);
                bookRepository.deleteById(id);
                return new BaseResponse("Book: "+name+" is deleted", convertBookResponse(book),
                        true,LocalDateTime.now());
            }
            return new BaseResponse("Book: "+name+" is not available",null,
                    false,LocalDateTime.now());
        }catch(Exception e) {
            return new BaseResponse("Fail to delete a book with name: " + name, e.getMessage(),
                    false,LocalDateTime.now());
        }
    }

    @Override
    public Boolean updateBookCount(Integer count, String bookName) {
        try{
            Book checking = bookRepository.findBookByName(bookName);
            if (checking != null){
                checking.setBookCount(count);
                bookRepository.save(checking);
                return true;
            }
            return false;
        }catch (Exception e){
            log.error("Error: "+e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public Integer findBookAvailability(Long bookId,Long authorId) {
        try{
            return bookRepository.findBookAvailability(bookId,authorId);
        }catch (Exception e){
            log.error("Error: {}",e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public BigDecimal getBookPrice(Long id) {
        try {
            return bookRepository.getBookPrice(id);
        }catch (Exception e){
            log.error("Error: {}",e.getLocalizedMessage());
            return null;
        }

    }

    @Override
    public Book findBookById(Long id) {
        try {
            return bookRepository.findBookById(id);
        }catch (Exception e){
            log.error("Error: {}",e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public Book findBookByName(String name) {
        try {
            return bookRepository.findBookByName(name);
        }catch (Exception e){
            log.error("Error: {}",e.getLocalizedMessage());
            return null;
        }
    }

    @Override
    public BaseResponse getBookByName(String name) {
        try{
            return new BaseResponse("Here is the book",
                    convertBookResponse(bookRepository.findBookByName(name)),
                    true,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to search",e.getMessage(),false,LocalDateTime.now());
        }
    }

    @Override
    public List<Book> findBookStock() {
        try{
            return bookRepository.findBooksByBookCountGreaterThan(0);
        }catch (Exception e){
            log.error("Error: {}",e.getMessage());
            return null;
        }
    }
    @Override
    public BookResponse convertBookResponse(Book book){
        return new BookResponse(book.getName(),authorService.getAuthorNameById(book.getAuthorId()),
                                book.getDateOfRelease(),book.getPrice(),book.getBookCount());
    }

}
