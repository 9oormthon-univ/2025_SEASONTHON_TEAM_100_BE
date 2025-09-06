package com.example.nextvalue.dto;

import java.util.List;

public record GptRequest(String model, List<Message> messages) {
    public record Message(String role, List<Content> content) {}
    public sealed interface Content permits TextContent, ImageContent {}
    public record TextContent(String type, String text) implements Content {}
    public record ImageContent(String type, ImageUrl image_url) implements Content {}
    public record ImageUrl(String url) {}
}
