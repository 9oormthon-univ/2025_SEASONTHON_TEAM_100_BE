package com.example.nextvalue.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

// 요청을 쿼리파라미터가 아닌 JSON 바디로 보내고 싶다면
public record WalkRequest(@NotNull @Min(0) Integer todayCount) {}

