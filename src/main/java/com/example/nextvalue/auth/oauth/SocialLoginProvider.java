package com.example.nextvalue.auth.oauth;

import com.example.nextvalue.config.exception.CustomException;
import com.example.nextvalue.config.exception.ErrorCode;

public enum SocialLoginProvider {
    LOCAL,
    GOOGLE;

    public static SocialLoginProvider from(String provider) {
        return switch (provider.toLowerCase()) {
            case "google" -> GOOGLE;
            case "local" -> LOCAL;
            default -> throw new CustomException(ErrorCode.UNSUPPORTED_OAUTH_PROVIDER);
        };
    }
}
