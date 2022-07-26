package com.onlineBookShop.bookshopSystem.constants;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Tungct in 2/18/2021
 */
@Component
@Slf4j
public class UtilImp implements Util {

    ObjectMapper objectMapper;

    public UtilImp() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.objectMapper.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
        this.objectMapper.registerModule(new JavaTimeModule());
//        this.objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        this.objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        this.objectMapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    }

    @Override
    public String normalizeIsdn(String isdn) {
        if (isdn == null) {
            return null;
        }
        isdn = isdn.trim();
        if (isdn.startsWith("95")) {
            return isdn;
        }
        if (isdn.startsWith("09")) {
            return "95" + isdn.substring(1);
        }

        if (isdn.startsWith("9")) {
            return "95" + isdn;
        }

        if (isdn.startsWith("+95")) {
            return isdn.substring(1);
        }

        if (isdn.startsWith("0095")) {
            return isdn.substring(2);
        }

        return isdn;
    }

    @Override
    public String toIsdn(String number) {
        String isdn = "";
        if (number == null) {
            return isdn;
        } else {
            isdn = number;
        }
        if (number.startsWith("09")) {
            isdn = number.substring(1);
        } else if (number.startsWith("+959")) {
            isdn = number.substring(3);
        } else if (number.startsWith("959")) {
            isdn = number.substring(2);
        }
        return isdn;
    }

    @Override
    public String toMsisdn(String number) {
        return "95" + toIsdn(number);
    }

    @Override
    public boolean isMytelNumber(String number) {
        String isdn = toIsdn(number);
        return isdn.startsWith("965") ||
                isdn.startsWith("966") ||
                isdn.startsWith("967") ||
                isdn.startsWith("968") ||
                isdn.startsWith("969");
    }

    @Override
    public String toJson(Object object) {
        ObjectMapper ow = new ObjectMapper();
        String json;
        try {
            json = ow.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.warn("Cannot convert java object to json string", e);
            json = object.toString();
        }
        return json;
    }

    @Override
    public String localDateTimeToString(LocalDateTime dateTime) {
        String DATE_YYYY_MM_DD_HH_MM_SS_dash = "yyyy-MM-dd HH:mm:ss";
        return dateTime.format(DateTimeFormatter.ofPattern(DATE_YYYY_MM_DD_HH_MM_SS_dash));
    }

    @Override
    public LocalDateTime stringToLocalDateTime(String timeString) {
        return LocalDateTime.parse(timeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @Override
    public LocalDateTime longToLocalDateTime(long timeLong) {
        return LocalDateTime.parse(String.valueOf(timeLong), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    @Override
    public CharSequence standardizeDateString(String dateString) {
        String month = String.format("%02d", Integer.valueOf(dateString.split("/")[0]));
        String day = String.format("%02d", Integer.valueOf(dateString.split("/")[1]));
        String year = String.format("%04d", Integer.valueOf(dateString.split("/")[2]));
        return month + "/" + day + "/" + year;
    }

    @Override
    public String localdateToPartition(LocalDate localDate) {
        return "data" + localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    @Override
    public <T> T toObject(String json, Class t) {
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return (T) objectMapper.readValue(json, t);
        } catch (JsonProcessingException e) {
            log.error("Cannot convert to object", e);
        }
        return null;
    }

    @Override
    public String encodeFileToBase64Binary(String imgUrl) {
        if(imgUrl == null){
            return null;
        }
        try {
            File file = new File(imgUrl);
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            String imageData= new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
            return imageData;
        } catch (Exception e) {
            log.info("Exception : ", e);
        }
        return null;
    }
}
