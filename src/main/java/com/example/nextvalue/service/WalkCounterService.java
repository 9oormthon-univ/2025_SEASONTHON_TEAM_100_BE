package com.example.nextvalue.service;

import com.example.nextvalue.config.exception.CustomException;
import com.example.nextvalue.config.exception.ErrorCode;
import com.example.nextvalue.dto.WalkResponse;
import com.example.nextvalue.entity.Member;
import com.example.nextvalue.memberdetail.MemberDetails;
import com.example.nextvalue.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class WalkCounterService {

    private final MemberRepository memberRepository;

    // 인메모리 캐시 (memberId → 마지막 보고 값)
    private final Map<Long, LastReport> cache = new ConcurrentHashMap<>();
    // 회원별 동시성 제어용 가벼운 락 오브젝트 풀
    private final Map<Long, Object> locks = new ConcurrentHashMap<>();

    // 한국 시간 기준
    private static final ZoneId KST = ZoneId.of("Asia/Seoul");

    @Transactional
    public WalkResponse applyTodayCount(MemberDetails memberDetails, int todayCount) {
        Member member = memberRepository.findByEmail(memberDetails.getEmail()).orElseThrow(()->new CustomException(ErrorCode.USER_NOT_FOUND));
        if (todayCount < 0) todayCount = 0;

        Object lock = locks.computeIfAbsent(member.getId(), k -> new Object());

        synchronized (lock) {
            LocalDate today = LocalDate.now(KST);
            LastReport prev = cache.get(member.getId());

            boolean isNewDay = prev != null && !today.equals(prev.date());
            int delta;

            if (prev == null) {
                // 첫 보고 → 오늘값 전부를 증가분으로 처리
                delta = todayCount;
            } else if (isNewDay) {
                // 날짜 바뀜 → 증가분 적용하지 않음(요구사항)
                delta = 0;
            } else {
                // 같은 날 → 증가한 만큼만 더함(감소시 0)
                delta = Math.max(0, todayCount - prev.todayCount());
            }

            Member m = memberRepository.findById(member.getId())
                    .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

            if (delta > 0) {
                m.increaseTotalWalkCnt(delta); // m.setTotalWalkCnt(m.getTotalWalkCnt()+delta) 래핑 메서드 추천
            }

            // 캐시는 항상 최신으로 갱신
            cache.put(member.getId(), new LastReport(today, todayCount, Instant.now()));

            return new WalkResponse(
                    m.getTotalWalkCnt(),
                    delta,
                    isNewDay,
                    isNewDay ? "날짜가 바뀌어 증가분을 반영하지 않았습니다." : "증가분을 반영했습니다."
            );
        }
    }



    private record LastReport(LocalDate date, int todayCount, Instant lastSeen) {}
}
