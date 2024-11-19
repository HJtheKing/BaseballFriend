package com.teamjhw.bestfriend.entity;

import com.teamjhw.bestfriend.global.exception.ErrorCode;
import com.teamjhw.bestfriend.global.exception.exceptionType.CharacterException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ItemCategory {
    Head(0),
    Body(1),
    Arm(2);

    private final int key;

    /*
     * 정수 -> Enum 변환 메서드
     */
    public static ItemCategory fromKey(int key) {
        if (key > 2 || key < 0) {
            throw new CharacterException(ErrorCode.UNDEFINED_ITEM_CATEGORY);
        }
        return ItemCategory.values()[key];
    }


}
