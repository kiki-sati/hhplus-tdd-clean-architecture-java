package com.hhplus.lecture.interfaces.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LectureEnrollmentResponseDto {
	private Long id; // 수강 신청 ID
	private Long lectureId; // 강의 ID
	private Long userId; // 사용자 ID
	private LocalDate enrollmentDate; // 수강 신청 날짜

}