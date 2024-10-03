package com.hhplus.lecture.interfaces.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LectureResponseDto {
	private Long id; // 강의 ID
	private String name; // 강의 이름
	private String lecturerName; // 강연자 이름
	private LocalDate date; // 강의 날짜
}