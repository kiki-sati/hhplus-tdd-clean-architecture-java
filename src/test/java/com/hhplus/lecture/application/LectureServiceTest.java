package com.hhplus.lecture.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.hhplus.lecture.domain.Lecture;
import com.hhplus.lecture.domain.LectureEnrollment;
import com.hhplus.lecture.domain.repository.LectureEnrollmentRepository;
import com.hhplus.lecture.domain.repository.LectureRepository;
import com.hhplus.lecture.interfaces.exception.ErrorMessage;

class LectureServiceTest {
	private LectureRepository lectureRepository;
	private LectureEnrollmentRepository lectureEnrollmentRepository;
	private LectureService lectureService;

	@BeforeEach
	void setUp() {
		lectureRepository = mock(LectureRepository.class);
		lectureEnrollmentRepository = mock(LectureEnrollmentRepository.class);
		lectureService = new LectureService(lectureRepository, lectureEnrollmentRepository);
	}

	@Test
	void 강의신청_성공() {
		Long lectureId = 1L;
		Long userId = 1L;
		LocalDate date = LocalDate.of(2024, 10, 1);
		Lecture lecture = Lecture.create("동시성 제어를 하는 방법", "김키키", date);

		// 강의 존재 설정
		when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));
		when(lectureEnrollmentRepository.findByLectureId(lectureId)).thenReturn(Collections.emptyList());
		when(lectureEnrollmentRepository.existsByLectureIdAndUserId(lectureId, userId)).thenReturn(false);

		String result = lectureService.enrollInLecture(lectureId, userId, date);

		assertEquals("강의 등록 성공", result);
		verify(lectureEnrollmentRepository, times(1)).save(any(LectureEnrollment.class));
	}

	@Test
	void 실패_강의신청_강의가존재하지않음() {
		Long lectureId = 1L;
		Long userId = 1L;
		LocalDate date = LocalDate.of(2024, 10, 1);

		when(lectureRepository.findById(lectureId)).thenReturn(Optional.empty());

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			lectureService.enrollInLecture(lectureId, userId, date);
		});

		assertEquals(ErrorMessage.LECTURE_NOT_FOUND.getMessage(), exception.getMessage());
	}

	@Test
	void 실패_강의신청_날짜불일치() {
		Long lectureId = 1L;
		Long userId = 1L;
		LocalDate date = LocalDate.of(2024, 10, 1);
		Lecture lecture = Lecture.create("동시성 제어를 하는 방법", "김키키", LocalDate.of(2024, 10, 2));

		when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));

		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			lectureService.enrollInLecture(lectureId, userId, date);
		});

		assertEquals(ErrorMessage.INVALID_DATE.getMessage(), exception.getMessage());

	}

	@Test
	void 실패_강의신청_정원초과() {
		Long lectureId = 1L;
		Long userId = 1L;
		LocalDate date = LocalDate.of(2024, 10, 1);
		Lecture lecture = Lecture.create("동시성 제어를 하는 방법", "김키키", date); // 최대 인원 수는 30

		// 강의 존재 설정
		when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));

		// 강의에 이미 등록된 사용자가 30명이라고 가정
		when(lectureEnrollmentRepository.findByLectureId(lectureId)).thenReturn(
			IntStream.range(0, 30) // 30명의 등록자 생성
				.mapToObj(i -> LectureEnrollment.create(lectureId, (long)i, LocalDate.now()))
				.collect(Collectors.toList())
		);

		// 강의의 최대 수용 인원이 30명으로 고정되므로, 31번째 사용자가 등록 시도할 때 예외가 발생해야 함
		IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
			lectureService.enrollInLecture(lectureId, userId + 30, date); // 31번째 등록 시도
		});

		assertEquals(ErrorMessage.LECTURE_FULL.getMessage(), exception.getMessage());
	}

	@Test
	void 실패_강의신청_중복신청() {
		Long lectureId = 1L;
		Long userId = 1L;
		LocalDate date = LocalDate.of(2024, 10, 1);
		Lecture lecture = Lecture.create("Test Lecture", "John Doe", date); // 강의 객체 생성

		// 강의 존재 설정
		when(lectureRepository.findById(lectureId)).thenReturn(Optional.of(lecture));

		// 강의에 이미 등록된 사용자가 있다고 가정
		when(lectureEnrollmentRepository.findByLectureId(lectureId)).thenReturn(
			Arrays.asList(
				LectureEnrollment.create(lectureId, userId, LocalDate.now()) // 이미 등록된 사용자 추가
			)
		);

		// 중복 신청이 발생하는 경우를 설정
		when(lectureEnrollmentRepository.existsByLectureIdAndUserId(lectureId, userId)).thenReturn(true);

		// 예외가 발생해야 함
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
			lectureService.enrollInLecture(lectureId, userId, date);
		});

		assertEquals(ErrorMessage.USER_ALREADY_ENROLLED.getMessage(), exception.getMessage());
	}

	@Test
	void 성공_특강목록조회() {
		// Given
		Lecture lecture1 = Lecture.create("강의 1", "강사 1", LocalDate.now());
		Lecture lecture2 = Lecture.create("강의 2", "강사 2", LocalDate.now());
		when(lectureRepository.findAll()).thenReturn(Arrays.asList(lecture1, lecture2));

		// When
		List<Lecture> lectures = lectureService.getOpenLectures();

		// Then
		assertNotNull(lectures);
		assertEquals(2, lectures.size());
		assertEquals("강의 1", lectures.get(0).getName());
		assertEquals("강의 2", lectures.get(1).getName());
	}

	@Test
	void 성공_특강신청완료목록조회() {
		// Given
		Long userId = 1L;
		LectureEnrollment enrollment1 = LectureEnrollment.create(1L, userId, LocalDate.now());
		LectureEnrollment enrollment2 = LectureEnrollment.create(2L, userId, LocalDate.now());
		when(lectureEnrollmentRepository.findByUserId(userId)).thenReturn(Arrays.asList(enrollment1, enrollment2));

		// When
		List<LectureEnrollment> enrollments = lectureService.getEnrollmentsByUserId(userId);

		// Then
		assertNotNull(enrollments);
		assertEquals(2, enrollments.size());
		assertEquals(userId, enrollments.get(0).getUserId());
		assertEquals(userId, enrollments.get(1).getUserId());
	}

}


