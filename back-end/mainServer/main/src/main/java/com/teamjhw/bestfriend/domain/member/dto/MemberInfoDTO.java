package com.teamjhw.bestfriend.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

public class MemberInfoDTO {

    @Getter
    @Builder
    public static class Request {

        private String name;
        private Boolean isBriefingAllowed;
        private Boolean isBroadcastAllowed;
        private String favoriteTeam;

        public static Request of(String name, Boolean isBriefingAllowed, Boolean isBroadcastAllowed, String favoriteTeam) {
            return Request.builder()
                    .name(name)
                    .isBriefingAllowed(isBriefingAllowed)
                    .isBroadcastAllowed(isBroadcastAllowed)
                    .favoriteTeam(favoriteTeam)
                    .build();
        }
    }
}
