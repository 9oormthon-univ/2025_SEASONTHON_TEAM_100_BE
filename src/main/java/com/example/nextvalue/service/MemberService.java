package com.example.nextvalue.service;

import com.example.nextvalue.entity.Member;
import com.example.nextvalue.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public List<Member> getFriends(Member member) {
        return member.getFriends();
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id).orElse(null);
    }

    public void addFriend(Member member) {
        member.getFriends().add(member);
    }
}
