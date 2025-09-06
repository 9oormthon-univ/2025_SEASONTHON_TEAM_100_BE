package com.example.nextvalue.controller;

import com.example.nextvalue.service.GptVisionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
public class GptVisionController {

    private final GptVisionService gptVisionService;

    @PostMapping("/vision/ask")
    public Mono<String> ask(
            @RequestParam("image") MultipartFile image,
            @RequestParam("prompt") String prompt) {

        try {
            return gptVisionService.askGptAboutImage(image, prompt);
        } catch (IOException e) {
            e.printStackTrace();
            return Mono.just("Error processing image: " + e.getMessage());
        }
    }
}