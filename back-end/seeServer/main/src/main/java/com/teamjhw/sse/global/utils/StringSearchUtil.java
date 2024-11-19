package com.teamjhw.sse.global.utils;

import java.util.Arrays;

/**
 * 보이어-무어 문자열 탐색 알고리즘
 */
public class StringSearchUtil {
    private int[] buildLastTable(String pattern) {
        final int ALPHABET_SIZE = 256; // 전체 아스키 문자 개수
        int[] lastTable = new int[ALPHABET_SIZE];

        Arrays.fill(lastTable, -1);

        for (int i = 0; i < pattern.length(); i++) {
            lastTable[pattern.charAt(i)] = i;
        }

        return lastTable;
    }

    public boolean search(String text, String pattern) {
        int[] lastTable = buildLastTable(pattern);
        int textLength = text.length();
        int patternLength = pattern.length();
        int skip;

        for (int i = 0; i <= textLength - patternLength; i += skip) {
            skip = 0;

            // 패턴을 끝에서부터 검사
            for (int j = patternLength - 1; j >= 0; j--) {
                if (pattern.charAt(j) != text.charAt(i + j)) {
                    // 일치하지 않으면 이동 거리를 계산
                    skip = Math.max(1, j - lastTable[text.charAt(i + j)]);
                    break;
                }
            }

            // 패턴이 일치하는 경우 true
            if (skip == 0) {
                return true;
            }
        }
        // 문자열 끝까지 못 찾으면 false
        return false;
    }
}
