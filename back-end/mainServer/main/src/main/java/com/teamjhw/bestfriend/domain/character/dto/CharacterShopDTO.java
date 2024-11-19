package com.teamjhw.bestfriend.domain.character.dto;

import com.teamjhw.bestfriend.entity.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CharacterShopDTO {

//  api /character/shop
    @Getter
    @Builder
    public static class Response {
        private List<ShopCharacterInfo> characterList;
        private List<ShopItemInfo> itemList;
        private List<ShopBackgroundInfo> backgroundList;

        @Builder
        @Getter
        public static class ShopCharacterInfo {
            private Long characterSerialNumber;
            private String characterName;

            public static ShopCharacterInfo of(CharacterInfo characterInfo) {
                return ShopCharacterInfo.builder()
                        .characterSerialNumber(characterInfo.getCharacterInfoSerialNumber())
                        .characterName(characterInfo.getCharacterName())
                        .build();
            }
        }

        @Builder
        @Getter
        public static class ShopItemInfo {
            private Long itemSerialNumber;
            private Long characterSerialNumber;
            private int itemCategory;
            private Long price;
            private String teamName;

            public static ShopItemInfo of(Item item) {
                return ShopItemInfo.builder()
                        .itemSerialNumber(item.getItemSerialNumber())
                        .characterSerialNumber(item.getCharacterInfo().getCharacterInfoSerialNumber())
                        .itemCategory(item.getItemCategory().getKey())
                        .price(item.getPrice())
                        .teamName(item.getTeam().getTeamName())
                        .build();
            }
        }

        @Builder
        @Getter
        public static class ShopBackgroundInfo {
            private Long backgroundSerialNumber;
            private Long price;

            public static ShopBackgroundInfo of(Background background) {
                return ShopBackgroundInfo.builder()
                        .backgroundSerialNumber(background.getBackgroundSerialNumber())
                        .price(background.getPrice())
                        .build();
            }

        }

        public static Response of(
                List<CharacterInfo> characters,
                List<Item> items,
                List<Background> backgrounds
        ) {
            List<ShopCharacterInfo> shopCharacterInfoList = characters.stream()
                    .map(ShopCharacterInfo::of)
                    .collect(Collectors.toList());

            List<ShopItemInfo> shopItemInfoList = items.stream()
                    .map(ShopItemInfo::of)
                    .collect(Collectors.toList());

            List<ShopBackgroundInfo> shopBackgroundInfoList = backgrounds.stream()
                    .map(ShopBackgroundInfo::of)
                    .collect(Collectors.toList());

            return Response.builder()
                    .characterList(shopCharacterInfoList)
                    .itemList(shopItemInfoList)
                    .backgroundList(shopBackgroundInfoList)
                    .build();
        }
    }
}
