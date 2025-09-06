package com.example.nextvalue.util;

import com.example.nextvalue.enums.CountryCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class LocationService {

    public CountryCode getCountryCodesOrdered(long totalWalkCnt) {
        List<CountryCode> countryCodes;
        countryCodes= CountryCode.fillCountryList();

        for (CountryCode countryCode : countryCodes) {
            //4만에 나라변경
            totalWalkCnt -= 40000;
            if (totalWalkCnt <= 0) {
                return countryCode;
            }
        }return countryCodes.get(countryCodes.size()-1);

    }
}
