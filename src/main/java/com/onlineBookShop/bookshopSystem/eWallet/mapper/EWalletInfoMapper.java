package com.onlineBookShop.bookshopSystem.eWallet.mapper;

import com.onlineBookShop.bookshopSystem.eWallet.entity.EWalletInfo;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Component
public class EWalletInfoMapper implements RowMapper<EWalletInfo> {

    @Override
    public EWalletInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
        EWalletInfo eWalletInfo = new EWalletInfo();
        eWalletInfo.setId(rs.getLong("id"));
        eWalletInfo.setOwnerId(rs.getLong("owner_id"));
        eWalletInfo.setAccountName(rs.getString("account_name"));
        eWalletInfo.setBalance(rs.getBigDecimal("balance"));
        eWalletInfo.setCreateAt(rs.getObject("create_at",LocalDateTime.class));
        return eWalletInfo;
    }
}
