package com.shopme.admin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeUtils {

    public static LocalDateTime convertToLocalDateTime(String input) {
        LocalDateTime result = null;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            result = LocalDateTime.parse(input, formatter);
        } catch (DateTimeParseException e) {
            // Handle the exception if the input string cannot be parsed
            e.printStackTrace();
        }
        return result;

    }
}