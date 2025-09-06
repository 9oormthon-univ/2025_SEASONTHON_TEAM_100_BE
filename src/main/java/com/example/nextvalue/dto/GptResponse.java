package com.example.nextvalue.dto;

import java.util.List;

// 응답용 DTO
public record GptResponse(List<Choice> choices) {
    public record Choice(Message message) {}
    public record Message(String role, String content) {}
}
