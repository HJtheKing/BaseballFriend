package com.teamjhw.bestfriend.domain.character.dto;

import com.teamjhw.bestfriend.entity.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BuyBackgroundDTO {
// /shop/buy/background
    @Getter
    @NoArgsConstructor
    public static class Request {
        private Long backgroundSerialNumber;

        public MemberBackground toEntity(Member member, Background background) {
            return MemberBackground.builder()
                    .background(background)
                    .member(member)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long gameMoney;

        public static Response of(Member member) {
            return Response.builder()
                    .gameMoney(member.getGameMoney())
                    .build();
        }
    }
}
