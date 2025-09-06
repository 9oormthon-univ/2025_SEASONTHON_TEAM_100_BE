package com.example.nextvalue.auth.oauth;

import com.example.nextvalue.entity.Member;
import com.example.nextvalue.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PrincipalDetailsService {

    private final MemberService memberService;

    public UserDetails loadUserById(Long id) {
        Member member = memberService.getMemberById(id);
        return new PrincipalDetails(member);
    }
} 