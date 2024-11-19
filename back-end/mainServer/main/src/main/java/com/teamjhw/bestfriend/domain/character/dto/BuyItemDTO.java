package com.teamjhw.bestfriend.domain.character.dto;

import com.teamjhw.bestfriend.entity.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BuyItemDTO {
    // /shop/buy/Item
    @Getter
    @NoArgsConstructor
    public static class Request {
        private Long itemSerialNumber;

        public MemberItem toEntity(Member member, MemberCharacter memberCharacter, Item item) {
            return MemberItem.builder()
                    .item(item)
                    .member(member)
                    .memberCharacter(memberCharacter)
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long gameMoney;

        public static BuyItemDTO.Response of(Member member) {
            return BuyItemDTO.Response.builder()
                    .gameMoney(member.getGameMoney())
                    .build();
        }
    }
}
