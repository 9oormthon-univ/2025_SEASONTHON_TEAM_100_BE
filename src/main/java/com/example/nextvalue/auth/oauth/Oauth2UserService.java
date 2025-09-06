package com.example.nextvalue.auth.oauth;

import com.example.nextvalue.entity.Member;
import com.example.nextvalue.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2UserService extends DefaultOAuth2UserService {
//    private final JwtTokenUtil jwtTokenUtil;
//    private final MemberRepository memberRepository;
//    private final HttpSession httpSession;
    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // userRequest에서 accessToken을 꺼내서 userInfoUri로 요청보내서 사용자 정보 받아옴
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId(); // kakao
        SocialLoginProvider socialLoginProvider = SocialLoginProvider.from(provider);

        OAuthAttributes attributes = OAuthAttributes.of(socialLoginProvider, oAuth2User.getAttributes());

        String email = provider + "_" + attributes.getEmail();
        String name = attributes.getName();

        Member member = memberService.findOrCreateSocialMember(email, name, socialLoginProvider); // 또는 GOOGLE


        return new PrincipalDetails(member, oAuth2User.getAttributes());
    }
}
