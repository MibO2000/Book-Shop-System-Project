package com.onlineBookShop.bookshopSystem.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


@Configuration
@ConfigurationProperties(prefix = "config")
@Data
@EnableScheduling
public class AppConfig {
    String fromMail;
    Datasource eWalletDatasource;
    EWalletInfo eWalletInfo;
    EWalletHistory eWalletHistory;
    public static class Datasource{
        private String sourceUrl;
        private String username;
        private String password;
        private String driverClassname;
        private String driveType;

        public String getSourceUrl() {
            return sourceUrl;
        }

        public void setSourceUrl(String sourceUrl) {
            this.sourceUrl = sourceUrl;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriverClassname() {
            return driverClassname;
        }

        public void setDriverClassname(String driverClassname) {
            this.driverClassname = driverClassname;
        }

        public String getDriveType() {
            return driveType;
        }

        public void setDriveType(String driveType) {
            this.driveType = driveType;
        }
    }
    public static class EWalletInfo{
        private String infoQuery;
        private String infoInsertQuery;
        private String infoUpdateQuery;
        private String updateBalanceQuery;
        private String updateNameQuery;
        private String infoDeleteQuery;
        private String infoAllQuery;
        private String addBalanceQuery;

        public String getAddBalanceQuery() {
            return addBalanceQuery;
        }

        public void setAddBalanceQuery(String addBalanceQuery) {
            this.addBalanceQuery = addBalanceQuery;
        }

        public String getUpdateNameQuery() {
            return updateNameQuery;
        }

        public void setUpdateNameQuery(String updateNameQuery) {
            this.updateNameQuery = updateNameQuery;
        }

        public String getInfoAllQuery() {
            return infoAllQuery;
        }

        public void setInfoAllQuery(String infoAllQuery) {
            this.infoAllQuery = infoAllQuery;
        }

        public String getInfoQuery() {
            return infoQuery;
        }

        public void setInfoQuery(String infoQuery) {
            this.infoQuery = infoQuery;
        }

        public String getInfoInsertQuery() {
            return infoInsertQuery;
        }

        public void setInfoInsertQuery(String infoInsertQuery) {
            this.infoInsertQuery = infoInsertQuery;
        }

        public String getInfoUpdateQuery() {
            return infoUpdateQuery;
        }

        public void setInfoUpdateQuery(String infoUpdateQuery) {
            this.infoUpdateQuery = infoUpdateQuery;
        }

        public String getUpdateBalanceQuery() {
            return updateBalanceQuery;
        }

        public void setUpdateBalanceQuery(String updateBalanceQuery) {
            this.updateBalanceQuery = updateBalanceQuery;
        }

        public String getInfoDeleteQuery() {
            return infoDeleteQuery;
        }

        public void setInfoDeleteQuery(String infoDeleteQuery) {
            this.infoDeleteQuery = infoDeleteQuery;
        }
    }
    public static class EWalletHistory{
        private String historyQuery;
        private String historyAllQuery;
        private String historyDeleteQuery;
        private String historyInsertQuery;
        private String historyQueryByDate;
        private String historyQueryForMail;
        private String historyQueryById;
        private String historyQueryByOwnerIdToDelete;

        public String getHistoryQueryByOwnerIdToDelete() {
            return historyQueryByOwnerIdToDelete;
        }

        public void setHistoryQueryByOwnerIdToDelete(String historyQueryByOwnerIdToDelete) {
            this.historyQueryByOwnerIdToDelete = historyQueryByOwnerIdToDelete;
        }

        public String getHistoryQueryForMail() {
            return historyQueryForMail;
        }

        public void setHistoryQueryForMail(String historyQueryForMail) {
            this.historyQueryForMail = historyQueryForMail;
        }

        public String getHistoryQueryById() {
            return historyQueryById;
        }

        public void setHistoryQueryById(String historyQueryById) {
            this.historyQueryById = historyQueryById;
        }

        public String getHistoryQueryByDate() {
            return historyQueryByDate;
        }

        public void setHistoryQueryByDate(String historyQueryByDate) {
            this.historyQueryByDate = historyQueryByDate;
        }

        public String getHistoryQuery() {
            return historyQuery;
        }

        public void setHistoryQuery(String historyQuery) {
            this.historyQuery = historyQuery;
        }

        public String getHistoryAllQuery() {
            return historyAllQuery;
        }

        public void setHistoryAllQuery(String historyAllQuery) {
            this.historyAllQuery = historyAllQuery;
        }

        public String getHistoryDeleteQuery() {
            return historyDeleteQuery;
        }

        public void setHistoryDeleteQuery(String historyDeleteQuery) {
            this.historyDeleteQuery = historyDeleteQuery;
        }

        public String getHistoryInsertQuery() {
            return historyInsertQuery;
        }

        public void setHistoryInsertQuery(String historyInsertQuery) {
            this.historyInsertQuery = historyInsertQuery;
        }
    }
}
