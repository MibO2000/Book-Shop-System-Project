package com.onlineBookShop.bookshopSystem.service.impl;

import com.onlineBookShop.bookshopSystem.entity.Author;
import com.onlineBookShop.bookshopSystem.payLoad.response.AllAuthorResponse;
import com.onlineBookShop.bookshopSystem.payLoad.response.AuthorResponse;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.repository.AuthorRepository;
import com.onlineBookShop.bookshopSystem.repository.BookRepository;
import com.onlineBookShop.bookshopSystem.service.AuthorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
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
            String checkName = authorRepository.findAuthorById(authorId).getName();
            return checkName != null;
        } catch (Exception e){
            log.error("Error: {}",e.getMessage());
            return false;
        }
    }

    @Override
    public String getAuthorNameById(Long authorId){
        try{
            return authorRepository.findAuthorById(authorId).getName();
        }catch (Exception e){
            log.error("Error: {}",e.getMessage());
            return null;
        }
    }
    @Override
    public BaseResponse createAuthor(Author author) {
        try{
            Author checking = authorRepository.findByName(author.getName());
            if (checking==null){
                Author createAuthor = authorRepository.save(new Author(author.getName(),author.getDob(),
                                                                       author.getAddress(), author.getEmail(),
                                                                       author.getPhone(), LocalDateTime.now()));
                return new BaseResponse("New Author "+createAuthor.getName() +" Created",
                        convertAuthorResponse(createAuthor),
                        true, LocalDateTime.now());
            }
            return new BaseResponse("Duplicate Available", convertAuthorResponse(author),
                    false,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to create user",e.getMessage(),false, LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse getAuthors(Integer pageNo, Integer pageSize, String sortBy) {
        try{
            Pageable pageable = PageRequest.of(pageNo - 1,pageSize, Sort.by(sortBy));
            List<AuthorResponse> pagedResult = authorRepository.findAll(pageable).stream()
                                                               .map(this::convertAuthorResponse).toList();
            AllAuthorResponse response = new AllAuthorResponse(pagedResult,pageNo,pageSize,sortBy);
            return new BaseResponse("Here is the list", response, true, LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to get Authors",e.getMessage(),false, LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse sortingAuthorList(Integer method) {
        try{
            return switch (method) {
                case 1 -> new BaseResponse("Here is the ascending sorted list",
                        authorRepository.ascendingAuthorSorting().stream().map(this::convertAuthorResponse),
                        true, LocalDateTime.now());
                case 2 -> new BaseResponse("Here is the descending sorted list",
                        authorRepository.descendingAuthorSorting().stream().map(this::convertAuthorResponse),
                        true, LocalDateTime.now());
                default -> new BaseResponse("Incorrect Input", null, false, LocalDateTime.now());
            };
        }catch (Exception e){
            return new BaseResponse("Fail to sort the author list",e.getMessage(),
                    false, LocalDateTime.now());
        }
    }

    @Override
    public BaseResponse updateAuthor(String name, Author author) {
        try{
            Author checkAuthor = authorRepository.findByName(name);
            log.info("");
            if (checkAuthor != null){
                checkAuthor.setName(author.getName());
                checkAuthor.setDob(author.getDob());
                checkAuthor.setPhone(author.getPhone());
                authorRepository.save(checkAuthor);
                return new BaseResponse("Updated author",
                        convertAuthorResponse(checkAuthor),true, LocalDateTime.now());
            }
            return new BaseResponse("No Author found with name: "+name,null,
                    false,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to update author with name: "+name,e.getMessage(),
                    false,LocalDateTime.now());
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
                return new BaseResponse("Deleted books and author with id: "+id, convertAuthorResponse(author),
                        true,LocalDateTime.now());

            }
            return new BaseResponse("No Author found with id: "+id,null,
                    false,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to delete author with id "+id,e.getMessage(),
                    false,LocalDateTime.now());
        }
    }

    public BaseResponse findAuthorByName(String name) {
        try{
            Author author = getAuthorByName(name);
            if (author == null){
                return new BaseResponse("no author with author name: "+name,
                        null,false,LocalDateTime.now());
            }
            return new BaseResponse("Here is the author ", convertAuthorResponse(author),
                                    true,LocalDateTime.now());
        }catch(Exception e){
            return  new BaseResponse("Fail to find author name: "+name,e.getMessage(),
                                    false,LocalDateTime.now());
        }
    }

    @Override
    public Author getAuthorByName(String name) {
        try {
            return authorRepository.findAuthorByName(name);
        }catch (Exception e){
            log.error("Error: {}",e.getMessage());
            return null;
        }
    }

    @Override
    public Author findById(long authorId) {
        try{
            return authorRepository.findAuthorById(authorId);
        }catch (Exception e){
            log.error("Error: {}",e.getMessage());
            return null;
        }
    }

    @Override
    public Long getAuthorIdByName(String name) {
        try{
            return authorRepository.getAuthorIdByName(name);
        }catch (Exception e){
            log.error("Error: {}",e.getMessage());
        }
        return null;
    }

    @Override
    public AuthorResponse convertAuthorResponse(Author author){
        return new AuthorResponse(
                author.getName(),author.getDob(),
                author.getAddress(),author.getEmail(),author.getPhone());
    }
}
