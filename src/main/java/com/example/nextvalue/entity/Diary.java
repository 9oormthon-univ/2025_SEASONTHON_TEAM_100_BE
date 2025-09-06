package com.example.nextvalue.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Diary {

    @Id @GeneratedValue
    private Long id;

    @Lob
    private String contents;

    private String imageURI;
}
