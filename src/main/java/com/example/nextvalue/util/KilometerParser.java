package com.example.nextvalue.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KilometerParser {
    public Long parseToKilometer(Long totalWalkCnt) {
        Long totalKilometer = (long)totalWalkCnt/133;
        return totalKilometer;
    }
}
