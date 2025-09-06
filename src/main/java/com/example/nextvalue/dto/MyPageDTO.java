package com.example.nextvalue.dto;

import lombok.Data;

@Data
public class MyPageDTO {

    private String username;
    private String email;
    private String imageURI;

    public MyPageDTO(String username, String email, String imageURI) {
        this.username = username;
        this.email = email;
        this.imageURI = imageURI;
    }


}
