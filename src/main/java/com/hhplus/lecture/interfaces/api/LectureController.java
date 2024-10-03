package com.hhplus.lecture.interfaces.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hhplus.lecture.application.LectureService;
import com.hhplus.lecture.domain.LectureEnrollment;
import com.hhplus.lecture.interfaces.dto.LectureEnrollmentResponseDto;
import com.hhplus.lecture.interfaces.dto.LectureRequestDto;
import com.hhplus.lecture.interfaces.dto.LectureResponseDto;

import lombok.RequiredArgsConstructor;

/**
 * The type Lecture controller.
 */
@RestController
@RequiredArgsConstructor
public class LectureController {

	private final LectureService lectureService;

	/**
	 * 특강 신청
	 *
	 * @param lectureId the lecture id
	 * @param requestDto the request dto
	 * @return the response entity
	 */
	@PostMapping("/{lectureId}/enroll")
	public ResponseEntity<String> enrollInLecture(@PathVariable Long lectureId, @RequestBody LectureRequestDto requestDto) {
		try {
			String result = lectureService.enrollInLecture(lectureId, requestDto.getUserId(), requestDto.getDate());
			return ResponseEntity.ok(result);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	/**
	 * 날짜별로 현재 신청 가능한 특강 목록을 조회
	 * @return the open lectures
	 */
	@GetMapping
	public ResponseEntity<List<LectureResponseDto>> getOpenLectures() {
		List<LectureResponseDto> openLectures = lectureService.getOpenLectures().stream()
			.map(lecture -> new LectureResponseDto(
				lecture.getId(),
				lecture.getName(),
				lecture.getLecturerName(),
				lecture.getDate()
			))
			.collect(Collectors.toList());

		return ResponseEntity.ok(openLectures);
	}

	/**
	 * 특강 신청 완료 목록 조회 API
	 * @param userId the user id
	 * @return the enrollments by user id
	 */
	@GetMapping("/enrollments/{userId}")
	public ResponseEntity<List<LectureEnrollmentResponseDto>> getEnrollmentsByUserId(@PathVariable Long userId) {
		List<LectureEnrollment> enrollments = lectureService.getEnrollmentsByUserId(userId);

		// LectureEnrollment 객체를 LectureEnrollmentResponseDto로 변환
		List<LectureEnrollmentResponseDto> responseDtos = enrollments.stream()
			.map(enrollment -> new LectureEnrollmentResponseDto(
				enrollment.getId(),
				enrollment.getLectureId(),
				enrollment.getUserId(),
				enrollment.getEnrollmentDate()
			))
			.collect(Collectors.toList());

		return ResponseEntity.ok(responseDtos);
	}
}
