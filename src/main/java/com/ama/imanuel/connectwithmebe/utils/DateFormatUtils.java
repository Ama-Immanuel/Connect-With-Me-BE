package com.ama.imanuel.connectwithmebe.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
@RequiredArgsConstructor
public class DateFormatUtils {
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public Date convertToDateSqlFromString(String dateStr) throws ParseException {
        return new Date(formatter.parse(dateStr).getTime());
    }

    public java.util.Date convertToDateFromString(String dateStr) throws ParseException {
        return formatter.parse(dateStr);
    }
}
