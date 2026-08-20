package com.naumoff.rnc.services.formaters;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;

@Service
public class DateFormatService {

    public String getDateString(LocalDateTime date) {
        return "19.02.2026";
    }
}
