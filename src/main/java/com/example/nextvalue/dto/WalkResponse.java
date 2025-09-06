package com.example.nextvalue.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WalkResponse {
    private long totalWalkCnt;   // 반영 후 총합
    private int appliedDelta;    // 이번에 실제로 더한 증가분
    private boolean skippedForNewDay; // 날짜 변경으로 미반영 여부
    private String message;
}
