package com.hhplus.lecture.domian;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *  특강 등록 정보
 */
@Entity
@Getter
@NoArgsConstructor
public class LectureEnrollment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // 등록 ID
	private Long lectureId; // 강의 ID
	private Long userId; // 사용자 ID
	private LocalDate enrollmentDate; // 등록 시간

	public static LectureEnrollment create(Long lectureId, Long userId, LocalDate enrollmentDate) {
		LectureEnrollment lectureEnrollment = new LectureEnrollment();
		lectureEnrollment.lectureId = lectureId;
		lectureEnrollment.userId = userId;
		lectureEnrollment.enrollmentDate = enrollmentDate;
		return lectureEnrollment;
	}
}
