package com.hitqz.scds.config;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String value) {
        if(value == null || value.trim().equals("") || value.equalsIgnoreCase("null")) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return formatter.parse(value);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
