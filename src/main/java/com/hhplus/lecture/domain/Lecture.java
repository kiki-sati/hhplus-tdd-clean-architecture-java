package com.hhplus.lecture.domain;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 특강 정보
 */
@Entity
@Getter
@NoArgsConstructor
public class Lecture {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 강의 ID
	private String name; // 강의 이름
	private String lecturerName; // 강연자 이름
	private LocalDate date; // 강의 날짜 및 시간

	@ElementCollection
	private Set<Long> registeredUsers = new HashSet<>(); // 등록된 사용자 ID 목록

	// 고정된 최대 인원 수
	private static final int MAX_CAPACITY = 30; // 정원 고정

	public static Lecture create(String name, String lecturerName, LocalDate date) {
		validateLecture(name, lecturerName, date);
		Lecture lecture = new Lecture();
		lecture.name = name;
		lecture.lecturerName = lecturerName;
		lecture.date = date;
		return lecture;
	}

	// 유효성 검증
	private static void validateLecture(String name, String lecturerName, LocalDate date) {
		if (name == null || name.isEmpty()) {
			throw new NullPointerException("강의명은 필수입니다.");
		}
		if (lecturerName == null || lecturerName.isEmpty()) {
			throw new NullPointerException("강사이름은 필수입니다.");
		}
		if (date == null) {
			throw new NullPointerException("강의 날짜는 필수입니다.");
		}
	}

	// 최대 인원 수 반환
	public int getMaxCapacity() {
		return MAX_CAPACITY;
	}
}