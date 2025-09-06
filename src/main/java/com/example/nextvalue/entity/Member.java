package com.example.nextvalue.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class Member {
    @GeneratedValue
    @Id
    private Long id;

    private String name;

    private String password;

    private String loginId;

    private String email;

    private String image;

    // provider : google이 들어감
    private String provider;

    // providerId : 구굴 로그인 한 유저의 고유 ID가 들어감
    private String providerId;

    ///  total 만보기 횟수
    @Column(nullable = false)
    private Long totalWalkCnt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    public void increaseTotalWalkCnt(int delta) {
        this.totalWalkCnt += delta;
    }
}
