package com.teamjhw.bestfriend.domain.character.dto;

import com.teamjhw.bestfriend.entity.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BuyCharacterDTO {
    // /shop/buy/Character
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long characterInfoSerialNumber;

        public MemberCharacter toEntity(Member member, CharacterInfo characterInfo) {
            return MemberCharacter.builder()
                    .characterInfo(characterInfo)
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

        public static BuyCharacterDTO.Response of(Member member) {
            return BuyCharacterDTO.Response.builder()
                    .gameMoney(member.getGameMoney())
                    .build();
        }
    }
}
