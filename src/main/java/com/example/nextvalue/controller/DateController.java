package com.example.nextvalue.controller;

import com.example.nextvalue.util.DateGetter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class DateController {

    @GetMapping("/datetimenow")
    public String datetimenow() {
        return DateGetter.getFormattedDateTime();
    }

}
