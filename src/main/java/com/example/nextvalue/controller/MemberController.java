package com.example.nextvalue.controller;


import com.example.nextvalue.apiPayload.ApiResponse;
import com.example.nextvalue.dto.MyPageDTO;
import com.example.nextvalue.entity.Member;
import com.example.nextvalue.memberdetail.MemberDetails;
import com.example.nextvalue.repository.MemberRepository;
import com.example.nextvalue.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/mypage")
    public ResponseEntity<ApiResponse<MyPageDTO>> getMyPage(@AuthenticationPrincipal MemberDetails memberDetails) {

        Member foundMember = memberService.findMemberByEmail(memberDetails.getEmail());
        MyPageDTO myPageDTO = new MyPageDTO(foundMember.getName(), foundMember.getEmail(), foundMember.getImage());

        return ResponseEntity.ok(ApiResponse.success(null,myPageDTO));
    }
}
