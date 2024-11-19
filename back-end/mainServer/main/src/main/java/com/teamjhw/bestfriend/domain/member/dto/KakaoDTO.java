package com.teamjhw.bestfriend.domain.member.dto;

import lombok.Getter;

public class KakaoDTO {

    @Getter
    public static class AccessToken{
        private String access_token;
        private String token_type;
        private String refresh_token;
        private int expires_in;
        private String scope;
        private int refresh_token_expires_in;
    }
}
