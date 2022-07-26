package com.onlineBookShop.bookshopSystem.eWallet.service.impl;

import com.onlineBookShop.bookshopSystem.config.AppConfig;
import com.onlineBookShop.bookshopSystem.eWallet.entity.EWalletHistory;
import com.onlineBookShop.bookshopSystem.eWallet.mapper.EWalletHistoryMapper;
import com.onlineBookShop.bookshopSystem.eWallet.service.EWalletHistoryService;
import com.onlineBookShop.bookshopSystem.entity.Book;
import com.onlineBookShop.bookshopSystem.entity.User;
import com.onlineBookShop.bookshopSystem.payLoad.response.AllEWalletHistoryResponse;
import com.onlineBookShop.bookshopSystem.payLoad.response.BaseResponse;
import com.onlineBookShop.bookshopSystem.payLoad.response.OrderResponse;
import com.onlineBookShop.bookshopSystem.service.BookService;
import com.onlineBookShop.bookshopSystem.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class EWalletHistoryServiceImpl implements EWalletHistoryService {
    private final JavaMailSender javaMailSender;
    private final EWalletHistoryMapper eWalletHistoryMapper;
    private final JdbcTemplate jdbcTemplate;
    private final AppConfig appConfig;
    private final UserService userService;
    private final BookService bookService;

    @Autowired
    public EWalletHistoryServiceImpl(JavaMailSender javaMailSender, EWalletHistoryMapper eWalletHistoryMapper,
                                     AppConfig appConfig, UserService userService, BookService bookService) {
        this.javaMailSender = javaMailSender;
        this.eWalletHistoryMapper = eWalletHistoryMapper;
        this.appConfig = appConfig;
        this.userService = userService;
        this.bookService = bookService;
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(appConfig.getEWalletDatasource().getSourceUrl());
        dataSource.setDriverClassName(appConfig.getEWalletDatasource().getDriverClassname());
        dataSource.setUsername(appConfig.getEWalletDatasource().getUsername());
        dataSource.setPassword(appConfig.getEWalletDatasource().getPassword());
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public BaseResponse getAllEWalletHistory() {
        try{
            List<EWalletHistory> historyList = jdbcTemplate.query(appConfig.getEWalletHistory().getHistoryAllQuery(),
                                                                  eWalletHistoryMapper);
            return new BaseResponse("Here is the list",
                    historyList.stream().map(this::convertAllEWalletHistory),
                    true,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to get All history",e.getMessage(),false, LocalDateTime.now());
        }
    }
    private AllEWalletHistoryResponse convertAllEWalletHistory(EWalletHistory eWalletHistory){
        Book book = bookService.findBookById(eWalletHistory.getBookId());
        User user = userService.getUserById(eWalletHistory.getOwnerId());
        return new AllEWalletHistoryResponse(
                eWalletHistory.getId(),user.getName(),book.getName(),eWalletHistory.getBeforeBalance(),
                eWalletHistory.getAfterBalance(),eWalletHistory.getBuyTime(),eWalletHistory.getBuyDate()
        );
    }

    @Override
    public BaseResponse getEachEWalletHistory() {
        try{
            return new BaseResponse("Here is the data",
                    eWalletHistoryList().stream().map(this::convertHistoryResponse), true,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to get User's wallet info",e.getMessage(),
                    false, LocalDateTime.now());
        }
    }
    @Override
    public List<EWalletHistory> eWalletHistoryList(){
        try{
            Long id = userService.getUserId();
            return jdbcTemplate.query(appConfig.getEWalletHistory().getHistoryQuery(),
                    new Object[]{id},eWalletHistoryMapper);
        }catch (Exception e){
            log.error("Error: {}",e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean deleteHistory(Long orderId) {
        try{
            int result = jdbcTemplate.update(appConfig.getEWalletHistory().getHistoryDeleteQuery(), orderId);
            return result > 0;
        }catch (Exception e){
            log.error("Error: "+e);
            return false;
        }
    }

    @Override
    public Boolean createHistory(EWalletHistory eWalletHistory) {
        if (eWalletHistory == null){
            return false;
        }
        try{
            jdbcTemplate.update(appConfig.getEWalletHistory().getHistoryInsertQuery(),
                    eWalletHistory.getOwnerId(),eWalletHistory.getBookId(),eWalletHistory.getBeforeBalance(),
                    eWalletHistory.getAfterBalance(),eWalletHistory.getBuyTime(),eWalletHistory.getBuyDate());
        }catch (Exception e){
            log.error("Error: "+ e);
            return false;
        }
        return true;
    }

    @Override
    public List<EWalletHistory> getHistoryByDate(LocalDate date) {
        if (date == null){
            log.error("No data found");
            return null;
        }
        Long id = userService.getUserId();
        try{
            return jdbcTemplate.query(
                    appConfig.getEWalletHistory().getHistoryQueryByDate(),new Object[]{id,date},eWalletHistoryMapper);
        }catch (Exception e){
            log.error("Error: "+ e);
            return null;
        }
    }
    @Override
    public EWalletHistory getHistoryByBookID(Long id){
        try{
            return jdbcTemplate.queryForObject
                    (appConfig.getEWalletHistory().getHistoryQueryById(),new Object[]{id},eWalletHistoryMapper);
        }catch(Exception e){
            log.error("Error: "+e);
            return null;
        }
    }

    @Override
    public BaseResponse getSpecificEWalletHistory(Long id) {
        try{
            List<EWalletHistory> eWalletHistory = jdbcTemplate.query(
                    appConfig.getEWalletHistory().getHistoryQueryById(), new Object[]{id},eWalletHistoryMapper);
            if (eWalletHistory.isEmpty()){
                return new BaseResponse("No history with that id: "+id,null,
                        false, LocalDateTime.now());
            }
            return new BaseResponse("Here is the data",
                    convertHistoryResponse(eWalletHistory.get(0)), true,LocalDateTime.now());
        }catch (Exception e){
            return new BaseResponse("Fail to get User's wallet info",e.getMessage(),
                    false, LocalDateTime.now());
        }
    }
    @Override
    public OrderResponse convertHistoryResponse(EWalletHistory eWalletHistory){
        try{
            Book book = bookService.findBookById(eWalletHistory.getBookId());
            return new OrderResponse(
                    eWalletHistory.getId(),book.getName(),eWalletHistory.getBeforeBalance(),
                    eWalletHistory.getAfterBalance(),eWalletHistory.getBuyTime(),eWalletHistory.getBuyDate()
            );
        }catch (Exception e){
            log.error("Error: {}",e.getMessage());
            return null;
        }
    }

    @Scheduled(cron = "04 20 9 * * ?")
    public void mailToUser(){
        try{
            List<EWalletHistory> eWalletHistoryList = jdbcTemplate.query(
                    appConfig.getEWalletHistory().getHistoryQueryForMail(),
                    new Object[]{LocalDate.now().minusDays(2)},eWalletHistoryMapper
            );
            log.info("List: "+eWalletHistoryList);
            if (eWalletHistoryList.isEmpty()){
                return;
            }
            for (EWalletHistory eWalletHistory:eWalletHistoryList){
                try{
                    Book book = bookService.findBookById(eWalletHistory.getBookId());
                    User user = userService.getUserById(eWalletHistory.getOwnerId());
                    MimeMessage msg = javaMailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(msg,true);
                    SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
                    simpleMailMessage.setFrom(appConfig.getFromMail());
                    simpleMailMessage.setTo(user.getEmail());
                    simpleMailMessage.setSubject("Notice for the book delivery");
                    String text = String.format("From MIB book store,\nBook, %s, will deliver today",book.getName());
                    simpleMailMessage.setText(text);
                    helper.setFrom(Objects.requireNonNull(simpleMailMessage.getFrom()));
                    helper.setTo(Objects.requireNonNull(simpleMailMessage.getTo()));
                    helper.setSubject(Objects.requireNonNull(simpleMailMessage.getSubject()));
                    helper.setText(Objects.requireNonNull(simpleMailMessage.getText()));
                    log.info("Sending mail.....");
                    javaMailSender.send(msg);
                    log.info("Mail sent");
                }catch (Exception e){
                    log.error("Error sending User with history{}, error: {}",eWalletHistory,e.getLocalizedMessage());
                }
            }
        }catch (Exception e){
            log.error("Error: "+e);
        }
    }
}
