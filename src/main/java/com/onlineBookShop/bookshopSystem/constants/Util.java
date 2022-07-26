package com.onlineBookShop.bookshopSystem.constants;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Tungct in 12/10/2020
 */
public interface Util {
    String normalizeIsdn(String isdn);
    String toIsdn(String number);
    String toMsisdn(String number);
    boolean isMytelNumber(String number);
    String localDateTimeToString(LocalDateTime dateTime);
    LocalDateTime stringToLocalDateTime (String timeString);
    LocalDateTime longToLocalDateTime (long timeLong);
    CharSequence standardizeDateString(String dateString);
    String toJson(Object object);
    String localdateToPartition(LocalDate localDate);
    <T> T toObject(String json, Class t);
    String encodeFileToBase64Binary(String imgUrl);

}
