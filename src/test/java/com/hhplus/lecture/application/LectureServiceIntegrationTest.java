package com.hhplus.lecture.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hhplus.lecture.domain.Lecture;
import com.hhplus.lecture.domain.repository.LectureRepository;
import com.hhplus.lecture.interfaces.exception.ErrorMessage;

@SpringBootTest
class LectureServiceIntegrationTest {

	@Autowired
	private LectureService lectureService;

	@Autowired
	private LectureRepository lectureRepository;

	private static final Logger logger = LoggerFactory.getLogger(LectureServiceIntegrationTest.class);

	/**
	 * 동시에 동일한 특강에 대해 40명이 신청했을 때, 30명만 성공하는 것을 검증
	 * @throws InterruptedException
	 */
	@Test
	void 강의신청_동시성_테스트() throws InterruptedException {
		Long lectureId = 21L; // 기존 강의 ID
		LocalDate requestedDate = LocalDate.of(2024, 10, 4); // 강의 날짜
		AtomicInteger successCount = new AtomicInteger(0); // 성공한 등록 수 카운터

		Runnable task = () -> {
			try {
				Long userId = Thread.currentThread().getId(); // 스레드 ID를 사용자 ID로 사용
				lectureService.enrollInLecture(lectureId, userId, requestedDate);
				successCount.incrementAndGet(); // 성공한 경우 카운트 증가
			} catch (Exception e) {
				logger.error("Error enrolling in lecture: ", e); // 에러 로그 추가
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

		// 성공한 등록 수가 정확히 30이어야 함
		logger.info("successCount:{}", successCount);
		assertThat(successCount.get()).isEqualTo(30); // 성공한 등록 수가 30이어야 함
	}

	@Test
	void 강의신청_중복신청_검증() {
		Long lectureId = 1L;
		Long userId = 1L;
		LocalDate date = LocalDate.of(2024, 10, 5); // 강의 날짜
		Lecture lecture = Lecture.create("Test Lecture", "John Doe", date); // 강의 객체 생성

		// 강의 존재 설정
		lectureRepository.save(lecture);

		// 같은 유저가 5번 신청하는 시나리오
		int successfulEnrollments = 0;

		for (int i = 0; i < 5; i++) {
			try {
				// 사용자 날짜를 강의 날짜와 동일하게 설정
				lectureService.enrollInLecture(lectureId, userId, date);
				successfulEnrollments++; // 성공 시 카운트 증가
			} catch (IllegalArgumentException e) {
				// 중복 신청 예외가 발생한 경우
				assertEquals(ErrorMessage.USER_ALREADY_ENROLLED, e.getMessage());
			}
		}

		// 성공한 신청 수가 1이어야 한다.
		assertEquals(1, successfulEnrollments);
	}
}
