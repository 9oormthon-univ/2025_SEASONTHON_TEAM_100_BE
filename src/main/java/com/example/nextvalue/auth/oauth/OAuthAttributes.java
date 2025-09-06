package com.example.nextvalue.auth.oauth;

import com.example.nextvalue.config.exception.CustomException;
import com.example.nextvalue.config.exception.ErrorCode;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private final String email;
    private final String name;

    public static OAuthAttributes of(SocialLoginProvider socialLoginProvider, Map<String, Object> attributes) {
        return switch (socialLoginProvider) {
            case GOOGLE -> ofGoogle(attributes);
            default -> throw new CustomException(ErrorCode.UNSUPPORTED_OAUTH_PROVIDER);
        };
    }


    private static OAuthAttributes ofGoogle(Map<String, Object> attributes) {
        return new OAuthAttributes(
                (String) attributes.get("email"),
                (String) attributes.get("name")
        );
    }

    public OAuthAttributes(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
