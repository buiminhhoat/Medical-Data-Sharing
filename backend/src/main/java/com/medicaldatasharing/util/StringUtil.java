package com.medicaldatasharing.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class StringUtil {
    public static String parseDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (date == null) return "";
        return simpleDateFormat.format(date);
    }

    public static Date createDate(String stringDate) throws ParseException {
        if (stringDate.isEmpty() || stringDate.equals("X")) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.parse(stringDate);
    }

    public static String anonymizeWord(String input, String searchWord, String replacement) {
        String searchPattern = "(?i)" + searchWord;
        return input.replaceAll(searchPattern, replacement);
    }
}

