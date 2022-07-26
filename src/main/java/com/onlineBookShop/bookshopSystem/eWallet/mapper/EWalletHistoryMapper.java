package com.onlineBookShop.bookshopSystem.eWallet.mapper;

import com.onlineBookShop.bookshopSystem.eWallet.entity.EWalletHistory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class EWalletHistoryMapper implements RowMapper<EWalletHistory> {
    @Override
    public EWalletHistory mapRow(ResultSet rs, int rowNum) throws SQLException {
        EWalletHistory eWalletHistory = new EWalletHistory();
        eWalletHistory.setId(rs.getLong("id"));
        eWalletHistory.setOwnerId(rs.getLong("owner_id"));
        eWalletHistory.setBookId(rs.getLong("book_id"));
        eWalletHistory.setBeforeBalance(rs.getBigDecimal("before_balance"));
        eWalletHistory.setAfterBalance(rs.getBigDecimal("after_balance"));
        eWalletHistory.setBuyTime(rs.getObject("buy_time", LocalTime.class));
        eWalletHistory.setBuyDate(rs.getObject("buy_date",LocalDate.class));
        return eWalletHistory;
    }
}
