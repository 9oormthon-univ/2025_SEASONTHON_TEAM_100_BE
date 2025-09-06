package com.example.nextvalue.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@Data
public class Member {
    @GeneratedValue
    @Id
    private Long id;

    private String name;

    private String passwd;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "diary_id")
    private Diary diary;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_id")
    private List<Member> friends;

}
