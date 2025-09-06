package com.example.nextvalue.entity;


import com.example.nextvalue.enums.CountryCode;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "landmark")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor @Builder
public class Landmark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 국가 엔티티와의 다대일 (Country 1 : N Landmark)
    // FK 컬럼명을 country_code 로 두고, Country의 PK(code: enum)와 조인
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_code", nullable = false)
    private Country country;

    // 요구사항: "랜드마크 entity에 국가 enum" → 동일 컬럼을 읽기 전용 ENUM으로 매핑
    // (조인은 country가 쓰기 담당, 이 필드는 조회 편의용)
    @Enumerated(EnumType.STRING)
    @Column(name = "country_code", insertable = false, updatable = false)
    private CountryCode countryCode;

    @Column(name = "city_name", nullable = false, length = 100)
    private String cityName;      // 도시이름

    @Column(name = "distance_km", nullable = false)
    private Integer km;           // km (정수로 관리, 필요시 Double/BigDecimal로 변경)

    @Column(name = "pedometer_count", nullable = false)
    private Integer pedometerCount; // 만보기 카운트(보)
}