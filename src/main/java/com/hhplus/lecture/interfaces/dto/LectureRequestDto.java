package com.hhplus.lecture.interfaces.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class LectureRequestDto {
	private Long userId;  // 신청하는 사용자 ID

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate date; // 신청할 특강일
}
