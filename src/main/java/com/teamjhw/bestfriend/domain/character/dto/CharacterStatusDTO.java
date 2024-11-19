package com.teamjhw.bestfriend.domain.character.dto;

import com.teamjhw.bestfriend.entity.Member;
import com.teamjhw.bestfriend.entity.MemberBackground;
import com.teamjhw.bestfriend.entity.MemberCharacter;
import com.teamjhw.bestfriend.entity.MemberItem;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CharacterStatusDTO {

//api /character/status
    @Getter
    @Builder
        public static class Response {
        private List<CharacterInfo> characterList;
        private List<BackgroundInfo> backgroundList;
        private Long characterSerialNumber;
        private Long headItemSerialNumber;
        private Long bodyItemSerialNumber;
        private Long armItemSerialNumber;
        private Long backgroundSerialNumber;

        @Builder
        @Getter
        public static class CharacterInfo {
            private Long characterSerialNumber;
            private String characterName;
            private List<ItemInfo> itemList;

            @Builder
            @Getter
            public static class ItemInfo {
                private Long itemSerialNumber;
                private int itemCategory;
                private String teamName;


                public static ItemInfo of(MemberItem memberItem) {

                    return ItemInfo.builder()
                            .itemSerialNumber(memberItem.getItem().getItemSerialNumber())
                            .itemCategory(memberItem.getItem().getItemCategory().getKey())
                            .teamName(memberItem.getItem().getTeam().getTeamName())
                            .build();
                }
            }

            public static CharacterInfo of(
                    MemberCharacter memberCharacter,
                    List<MemberItem> memberItems
            ) {
                // 해당 캐릭터에 맞는 아이템만 필터링
                List<MemberItem> filteredItems = memberItems.stream()
                        .filter(item -> item.getMemberCharacter().getId().equals(memberCharacter.getId()))
                        .collect(Collectors.toList());

                return CharacterInfo.builder()
                        .characterSerialNumber(memberCharacter.getCharacterInfo().getCharacterInfoSerialNumber())
                        .characterName(memberCharacter.getCharacterInfo().getCharacterName())
                        .itemList(filteredItems.stream()
                                .map(ItemInfo::of)
                                .collect(Collectors.toList()))
                        .build();
            }
        }

        @Builder
        @Getter
        public static class BackgroundInfo {
            private Long backgroundSerialNumber;

            public static BackgroundInfo of(MemberBackground memberBackground) {
                return BackgroundInfo.builder()
                        .backgroundSerialNumber(memberBackground.getBackground().getBackgroundSerialNumber())
                        .build();
            }

            public static List<BackgroundInfo> from(List<MemberBackground> memberBackgrounds) {
                return memberBackgrounds.stream()
                        .map(BackgroundInfo::of)
                        .collect(Collectors.toList());
            }
        }

        public static Response of(
                List<MemberCharacter> memberCharacters,
                List<MemberItem> memberItems,
                List<MemberBackground> memberBackgrounds,
                Member member
        ) {
            List<CharacterInfo> characterInfoList = memberCharacters.stream()
                    .map(character -> CharacterInfo.of(character, memberItems))
                    .collect(Collectors.toList());

            List<BackgroundInfo> backgroundInfoList = BackgroundInfo.from(memberBackgrounds);

            return Response.builder()
                    .characterList(characterInfoList)
                    .backgroundList(backgroundInfoList)
                    .characterSerialNumber(member.getSelectedCharacterSerialNumber())
                    .headItemSerialNumber(member.getWornHeadItemSerialNumber())
                    .bodyItemSerialNumber(member.getWornBodyItemSerialNumber())
                    .armItemSerialNumber(member.getWornArmItemSerialNumber())
                    .backgroundSerialNumber(member.getWornBackgroundSerialNumber())
                    .build();
        }
    }
}
