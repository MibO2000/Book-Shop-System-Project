package com.onlineBookShop.bookshopSystem.service.impl;

import com.onlineBookShop.bookshopSystem.entity.Author;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.repository.AuthorRepository;
import com.onlineBookShop.bookshopSystem.repository.BookRepository;
import com.onlineBookShop.bookshopSystem.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final BookRepository bookRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Boolean checkAuthor(Long authorId) {
        try{
            String checkName = authorRepository.authorName(authorId);
            return checkName != null;
        } catch (Exception e){
            return false;
        }
    }

    @Override
    public BaseResponse createAuthor(Author author) {
        try{
            List<Author> checking = authorRepository.findByName(author.getName());
            if (checking.isEmpty()){
                Author createAuthor = authorRepository.save(new Author(author.getName(),author.getDob(), author.getAddress(),
                                                 author.getEmail(), author.getPhone(), LocalDateTime.now()));
                return new BaseResponse("New Author "+createAuthor.getName() +" Created", createAuthor,
                                        true, LocalDateTime.now());
            }
            return new BaseResponse("Duplicate Available",author,false,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to create user",null,false, LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse getAuthors(Integer pageNo, Integer pageSize, String sortBy) {
        try{
            Pageable pageable = PageRequest.of(pageNo - 1,pageSize, Sort.by(sortBy));
            Page<Author> pagedResult = authorRepository.findAllAuthors(pageable);

            return new BaseResponse("Here is the list", pagedResult,true, LocalDateTime.now());
        }catch (Exception E){
            return new BaseResponse("Fail to get Authors",null,false, LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse sortingAuthorList(Integer method) {
        try{
            return switch (method) {
                case 1 -> new BaseResponse("Here is the ascending sorted list",
                        authorRepository.ascendingAuthorSorting(), true, LocalDateTime.now());
                case 2 -> new BaseResponse("Here is the descending sorted list",
                        authorRepository.descendingAuthorSorting(), true, LocalDateTime.now());
                default -> new BaseResponse("Incorrect Input", null, false, LocalDateTime.now());
            };
        }catch (Exception e){
            return new BaseResponse("Fail to sort the author list",null,false, LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse updateAuthor(Long id, Author author) {
        try{
            Optional<Author> checkAuthor = authorRepository.findById(id);
            log.info("");
            if (checkAuthor.isPresent()){
                Author updatedAuthor = checkAuthor.get();
                updatedAuthor.setName(author.getName());
                updatedAuthor.setDob(author.getDob());
                updatedAuthor.setPhone(author.getPhone());
                authorRepository.save(updatedAuthor);
                return new BaseResponse("Updated author with id: "+id,
                        authorRepository.getReferenceById(id),true, LocalDateTime.now());
            }
            return new BaseResponse("No Author found with id: "+id,null,false,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to update author with id "+id,null,false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse deleteAuthor(Long id) {
        try{
            Optional<Author> checkAuthor = authorRepository.findById(id);
            if (checkAuthor.isPresent()){
                Author author = authorRepository.getReferenceById(id);
                authorRepository.deleteById(id);
                List<Long> booksToDelete = bookRepository.idsForDelete(id);
                for (Long idsToDelete:booksToDelete){
                    bookRepository.deleteById(idsToDelete);
                }
                return new BaseResponse("Deleted books and author with id: "+id,author,true,LocalDateTime.now());

            }
            return new BaseResponse("No Author found with id: "+id,null,false,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to delete author with id "+id,null,false,LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse getAuthorByID(long id) {
        try{
            Author author = authorRepository.findAuthorById(id);
            if (author == null){
                return new BaseResponse("no author with author id: "+id,null,false,LocalDateTime.now());
            }
            return new BaseResponse("Here is the author "+id,author,
                                    true,LocalDateTime.now());
        }catch(Exception e){
            return  new BaseResponse("Fail to find author id: "+id,null,
                                    false,LocalDateTime.now());
        }
    }
}
