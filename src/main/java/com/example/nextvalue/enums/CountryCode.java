package com.example.nextvalue.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
public enum CountryCode {
    KR("KR", "대한민국"),
    JP("JP", "일본"),
    US("US", "미국"),
    GB("GB", "영국"),
    CN("CN", "중국"),
    FR("FR", "프랑스"),
    DE("DE", "독일"),
    IT("IT", "이탈리아");

    private final String code;         // ISO2 등 국가코드
    private final String displayName;  // 한국어 표시명

    public static CountryCode fromCode(String code) {
        for (CountryCode c : values()) {
            if (c.code.equalsIgnoreCase(code)) return c;
        }
        throw new IllegalArgumentException("Unknown country code: " + code);
    }
    public static List<CountryCode> fillCountryList() {
        // 불변 리스트(순서 유지)
        return Collections.unmodifiableList(Arrays.asList(CountryCode.values()));
    }
}