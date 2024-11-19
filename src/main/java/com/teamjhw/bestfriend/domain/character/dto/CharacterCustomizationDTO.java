package com.teamjhw.bestfriend.domain.character.dto;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CharacterCustomizationDTO {
    // /customization
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private Long characterInfoSerialNumber;
        private Long headItemSerialNumber;
        private Long bodyItemSerialNumber;
        private Long armItemSerialNumber;
        private Long backgroundSerialNumber;

    }

}
