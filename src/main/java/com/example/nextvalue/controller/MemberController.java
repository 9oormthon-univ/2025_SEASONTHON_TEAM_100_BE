package com.example.nextvalue.controller;


import com.example.nextvalue.apiPayload.ApiResponse;
import com.example.nextvalue.dto.DiaryEditDTO;
import com.example.nextvalue.dto.MyPageDTO;
import com.example.nextvalue.entity.Diary;
import com.example.nextvalue.entity.Member;
import com.example.nextvalue.memberdetail.MemberDetails;
import com.example.nextvalue.repository.MemberRepository;
import com.example.nextvalue.service.DiaryService;
import com.example.nextvalue.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final DiaryService diaryService;

    @GetMapping("/mypage")
    public ResponseEntity<ApiResponse<MyPageDTO>> getMyPage(@AuthenticationPrincipal MemberDetails memberDetails) {

        Member foundMember = memberService.findMemberByEmail(memberDetails.getEmail());
        MyPageDTO myPageDTO = new MyPageDTO(foundMember.getName(), foundMember.getEmail(), foundMember.getImage());

        return ResponseEntity.ok(ApiResponse.success(null,myPageDTO));
    }

    @PostMapping("/diary/save")
    public ResponseEntity<ApiResponse<?>> makeDiary(@RequestBody Diary diary){
        diaryService.makeDiary(diary);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/diary/{id}")
    public ResponseEntity<ApiResponse<Diary>> getDiaryById(@PathVariable("id") Long id){
        return ResponseEntity.ok(ApiResponse.success(null,diaryService.findDiaryById(id)));
    }

    @PostMapping("/diary/{id}/edit")
    public ResponseEntity<ApiResponse<?>> editDiary(@RequestBody DiaryEditDTO dto, @PathVariable("id") Long id){
        Diary diary = diaryService.findDiaryById(id);
        diary.setContents(dto.getContents());
        diary.setImageURI(diary.getImageURI());
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @GetMapping("/member/{id}/friends")
    public ResponseEntity<ApiResponse<List<Member>>> getFriends(@PathVariable("id") Long id){
        return ResponseEntity.ok(ApiResponse.success(null,
                memberService.getFriends(memberService.findMemberById(id))));
    }

    @PostMapping("/member/{id}/friends/add")
    public ResponseEntity<?> addFriend(@PathVariable("id") Long id) {
        Member foundMember = memberService.findMemberById(id);
        memberService.addFriend(foundMember);
        return ResponseEntity.ok(ApiResponse.success(null));
    }

}
