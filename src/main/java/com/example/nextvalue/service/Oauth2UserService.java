package com.example.nextvalue.service;

import com.example.nextvalue.dto.OAuthAttributes;
import com.example.nextvalue.entity.Member;
import com.example.nextvalue.jwt.JwtTokenUtil;
import com.example.nextvalue.repository.MemberRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2UserService extends DefaultOAuth2UserService {
    private final JwtTokenUtil jwtTokenUtil;
    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest
                .getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuthAttributes oauthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);

        if (oauthAttributes == null || oauthAttributes.getEmail() == null) {
            throw new OAuth2AuthenticationException("이메일 정보가 없습니다.");
        }

        log.info(oauthAttributes.getEmail());
        log.info(oauthAttributes.getName());
        log.info(oauthAttributes.getAttributes().toString());

        String email = oauthAttributes.getEmail();
        Member findMember = memberRepository.findByEmail(email);
        if (findMember == null) {
            Member member = new Member();
            member.setEmail(email);
            member.setName(oauthAttributes.getName());
            member.setImage(oauthAttributes.getImage());
            memberRepository.save(member);
        }

        Member returnMember = memberRepository.findByEmail(email);



        httpSession.setAttribute("loginMember", returnMember);




        return oAuth2User;
    }
}
