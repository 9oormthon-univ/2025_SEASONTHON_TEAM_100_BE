package com.example.nextvalue.service;

import com.example.nextvalue.dto.GptRequest;
import com.example.nextvalue.dto.GptResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class GptVisionService {

    private final WebClient webClient;
    private final String openaiApiKey;

    public GptVisionService(WebClient.Builder webClientBuilder, @Value("${openai.api-key}") String openaiApiKey) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1").build();
        this.openaiApiKey = openaiApiKey;
    }

    /**
     * GPT Vision API에 이미지와 프롬프트를 보내고 응답을 받습니다.
     * @param imageFile 사용자가 업로드한 이미지 파일
     * @param prompt 이미지에 대해 질문할 텍스트
     * @return GPT의 텍스트 답변
     */
    public Mono<String> askGptAboutImage(MultipartFile imageFile, String prompt) throws IOException {
        // 1. 이미지 파일을 Base64로 인코딩
        String base64Image = convertImageToBase64(imageFile);
        String imageUrl = "data:" + imageFile.getContentType() + ";base64," + base64Image;

        // 2. OpenAI API 요청 DTO 생성
        GptRequest.Content textContent = new GptRequest.TextContent("text", prompt);
        GptRequest.Content imageContent = new GptRequest.ImageContent("image_url", new GptRequest.ImageUrl(imageUrl));

        GptRequest.Message message = new GptRequest.Message("user", List.of(textContent, imageContent));
        GptRequest requestDto = new GptRequest("gpt-4o", List.of(message));

        // 3. WebClient를 사용하여 OpenAI API 호출
        return webClient.post()
                .uri("/chat/completions")
                .header("Authorization", "Bearer " + openaiApiKey)
                .bodyValue(requestDto)
                .retrieve()
                .bodyToMono(GptResponse.class)
                .map(response -> response.choices().get(0).message().content()); // 응답에서 텍스트만 추출
    }

    /**
     * MultipartFile을 Base64 문자열로 변환하는 헬퍼 메소드
     */
    private String convertImageToBase64(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        return Base64.getEncoder().encodeToString(bytes);
    }
}