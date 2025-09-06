package com.example.nextvalue.service;

import com.example.nextvalue.dto.DiaryEditDTO;
import com.example.nextvalue.entity.Diary;
import com.example.nextvalue.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public void makeDiary(Diary diary) {
        diaryRepository.save(diary);
    }

    public Diary findDiaryById(Long id) {
        return diaryRepository.findById(id).orElse(null);
    }


}
