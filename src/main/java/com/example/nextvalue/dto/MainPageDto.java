package com.example.nextvalue.dto;

import lombok.Builder;

public class MainPageDto {
    @Builder
    public record CurrentCountry(
            String memberName,
            String countryName,
            Long kilometerByCurrentCountry,
            String countryImageUrl
    ){}
}
