package com.onlineBookShop.bookshopSystem.service.impl;

import com.onlineBookShop.bookshopSystem.entity.Author;
import com.onlineBookShop.bookshopSystem.entity.Book;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.repository.BookRepository;
import com.onlineBookShop.bookshopSystem.service.AuthorService;
import com.onlineBookShop.bookshopSystem.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
            Page<Book> pagedResult = bookRepository.findAll(pageable);
            return new BaseResponse("Here is all books",pagedResult,true, LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to find all books",null,false, LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse createBook(Book book) {
        try{
            Long authorId = book.getAuthorId();
            if (!authorService.checkAuthor(authorId)){
                return new BaseResponse("Create the author first!",null,false, LocalDateTime.now());
            }
            List<Book> checking = bookRepository.findByName(book.getName());
            if (checking.isEmpty()){
                Book uploadedBook = bookRepository.save(new Book(book.getName(),book.getAuthorId(),
                        book.getDateOfRelease(),book.getPrice(), book.getBookCount(), LocalDateTime.now()));

                return new BaseResponse("New Book created",uploadedBook,true,LocalDateTime.now());
            }
            return new BaseResponse("Duplicate available",null,false,LocalDateTime.now());
        } catch (Exception e){
            return new BaseResponse("Fail to upload book",null,false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse getBooksByAuthorId(String name) {
        try{
            Author author = authorService.getAuthorByName(name);
            if (author == null){
                return new BaseResponse("Author not exists",null,false,LocalDateTime.now());
            }
            List<Book> bookList = bookRepository.findBooksByAuthorId(author.getId());
            if (bookList.isEmpty()){
                return new BaseResponse("The author has no book", null, true,LocalDateTime.now());
            }
            return new BaseResponse("Here is the list",bookList,true,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to get books by author id",null,false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse sortingBookList(Integer method) {
        try{
            return switch (method) {
                case 1 ->
                        new BaseResponse("Here is the ascending sorted list", bookRepository.ascendingBookSorting(), true, LocalDateTime.now());
                case 2 ->
                        new BaseResponse("Here is the descending sorted list", bookRepository.descendingBookSorting(), true, LocalDateTime.now());
                default -> new BaseResponse("Incorrect Input", null, false, LocalDateTime.now());
            };
        }catch (Exception e){
            return new BaseResponse("Fail to sort the book list",null,false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse updateBook(Long id, Book book) {
        try{
            Optional<Book> checkBook = bookRepository.findById(id);
            if(checkBook.isPresent()){
                Book updatedBook = checkBook.get();
                updatedBook.setName(book.getName());
                updatedBook.setPrice(book.getPrice());
                updatedBook.setBookCount(book.getBookCount());
                updatedBook.setDateOfRelease(book.getDateOfRelease());
                updatedBook.setAuthorId(book.getId());
                updatedBook.setCreatedAt(book.getCreatedAt());
                bookRepository.save(updatedBook);
                return new BaseResponse("Book "+ id + " is updated",updatedBook, true,LocalDateTime.now());
            }
            return new BaseResponse("Book "+id+" is not present",null,false,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to updaate a book with id:"+id,null,false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse deleteBook(Long id) {
        try{
            Optional<Book> checkBook = bookRepository.findById(id);
            if (checkBook.isPresent()){
                Book book = bookRepository.getReferenceById(id);
                bookRepository.deleteById(id);
                return new BaseResponse("Book "+id+" is deleted",book,true,LocalDateTime.now());
            }
            return new BaseResponse("Book "+id+" is not available",null,false,LocalDateTime.now());
        }catch(Exception e) {
            return new BaseResponse("Fail to delete a book with id:" + id, null, false,LocalDateTime.now());
        }
    }

    @Override
    public Boolean updateBookCount(Integer count, Long id) {
        try{
            Optional<Book> checking = bookRepository.findById(id);
            if (checking.isPresent()){
                Book updatedBook = checking.get();
                updatedBook.setBookCount(count);
                bookRepository.save(updatedBook);
                return true;
            }
            return false;
        }catch (Exception e){
            log.error("Error: "+e.getLocalizedMessage());
            return false;
        }
    }

    @Override
    public Integer findBookAvailability(Long id) {
        return bookRepository.findBookAvailability(id);
    }

    @Override
    public BigDecimal getBookPrice(Long id) {
        return bookRepository.getBookPrice(id);
    }

    @Override
    public Book findBookById(Long id) {
        return bookRepository.findBookById(id);
    }

    @Override
    public BaseResponse getBookByID(Long id) {
        try{
            return new BaseResponse("Here is the book with id: "+id,bookRepository.findById(id),
                    true,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to search",null,false,LocalDateTime.now());
        }
    }

    @Override
    public List<Book> findBookStock() {
        return bookRepository.findBooksByBookCountGreaterThan(0);
    }
}
