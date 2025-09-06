package com.example.nextvalue.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name = "gallery",
            joinColumns = @JoinColumn(name = "member_id")
    )
    @Column(name = "images_url") // 3. 리스트의 String 값이 저장될 컬럼 이름
    private List<String> Gallery;

    ///  total 만보기 횟수
    @Column(nullable = false)
    private Long totalWalkCnt;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<Member> friends;

    public void increaseTotalWalkCnt(int delta) {
        this.totalWalkCnt += delta;
    }
}
