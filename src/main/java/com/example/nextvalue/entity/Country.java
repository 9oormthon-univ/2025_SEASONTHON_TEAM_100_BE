package com.example.nextvalue.entity;


import com.example.nextvalue.entity.Country;
import com.example.nextvalue.enums.CountryCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "country")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class Country {

    // 국가 엔티티의 PK를 국가코드(enum)로 사용 (자연키)
    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "code", length = 8, nullable = false, updatable = false)
    private CountryCode code;      // 국가 enum

    @Column(name = "name", nullable = false, length = 100)
    private String name;           // 국가 이름 (표시용, ko/en 등 자유)

    private String countryImageUrl;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Landmark> landmarks = new ArrayList<>(); // 랜드마크 리스트

    // 편의 메서드
    public void addLandmark(Landmark lm) {
        landmarks.add(lm);
        lm.setCountry(this);
    }

    public void removeLandmark(Landmark lm) {
        landmarks.remove(lm);
        lm.setCountry(null);
    }
}
