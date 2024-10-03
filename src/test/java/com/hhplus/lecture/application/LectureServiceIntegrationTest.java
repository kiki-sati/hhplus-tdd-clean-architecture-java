package com.hhplus.lecture.application;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LectureServiceIntegrationTest {

	@Autowired
	private LectureService lectureService;

	/**
	 * 동시에 동일한 특강에 대해 40명이 신청했을 때, 30명만 성공하는 것을 검증
	 * @throws InterruptedException
	 */
	@Test
	void 강의신청_동시성_테스트() throws InterruptedException {
		Long lectureId = 1L; // 기존 강의 ID
		LocalDate requestedDate = LocalDate.of(2024, 10, 4); // 강의 날짜
		AtomicInteger successCount = new AtomicInteger(0); // 성공한 등록 수 카운터

		Runnable task = () -> {
			try {
				// 사용자 ID를 고유하게 생성
				Long userId = Thread.currentThread().getId(); // 스레드 ID를 사용자 ID로 사용
				lectureService.enrollInLecture(lectureId, userId, requestedDate);
				successCount.incrementAndGet(); // 성공한 경우 카운트 증가
			} catch (Exception e) {
			}
		};

		// 40개의 스레드 생성 및 시작
		List<Thread> threads = new ArrayList<>();
		for (int i = 0; i < 40; i++) {
			Thread thread = new Thread(task);
			threads.add(thread);
			thread.start();
		}

		// 모든 스레드가 종료될 때까지 대기
		for (Thread thread : threads) {
			thread.join();
		}

		// 성공한 등록 수가 30 이하여야 함
		assertThat(successCount.get()).isLessThanOrEqualTo(30);
	}

}
