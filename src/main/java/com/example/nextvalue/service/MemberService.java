package com.example.nextvalue.service;

import com.example.nextvalue.config.exception.CustomException;
import com.example.nextvalue.config.exception.ErrorCode;
import com.example.nextvalue.dto.MainPageDto;
import com.example.nextvalue.entity.Country;
import com.example.nextvalue.entity.Member;
import com.example.nextvalue.enums.CountryCode;
import com.example.nextvalue.repository.CountryRepository;
import com.example.nextvalue.repository.MemberRepository;
import com.example.nextvalue.util.KilometerParser;
import com.example.nextvalue.util.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final LocationService locationService;
    private final KilometerParser kilometerParser;
    private final CountryRepository countryRepository;



    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() ->new CustomException(ErrorCode.USER_NOT_FOUND));
    }

    public MainPageDto.CurrentCountry getCurrentCountry(String memberEmail) {
        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(() ->new CustomException(ErrorCode.USER_NOT_FOUND));
        Long totalWalkCnt = member.getTotalWalkCnt();
        CountryCode country = locationService.getCountryCodesOrdered(totalWalkCnt);
        Long kilometerByCurrentCountry = kilometerParser.parseToKilometer(totalWalkCnt)%30;
        Country currentCountry = countryRepository.findByCode(country.getCode()).orElseThrow(() ->new CustomException(ErrorCode.COUNTRY_NOT_FOUND));
        return MainPageDto.CurrentCountry.builder()
                .memberName(member.getName())
                .countryName(country.getDisplayName())
                .kilometerByCurrentCountry(kilometerByCurrentCountry)
                .countryImageUrl(currentCountry.getCountryImageUrl())
                .build();
    }
}
