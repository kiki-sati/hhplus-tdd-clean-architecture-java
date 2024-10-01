package com.hhplus.lecture.domian;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 특강 등록
 */
@Entity
@Getter
@NoArgsConstructor
public class LectureEnrollment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 참조 특강
	 */
	@ManyToOne
	@JoinColumn(name = "lecture_id", nullable = false)
	private Lecture lecture;

	/**
	 * 사용자
	 */
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	/**
	 * 최대 수용 인원
	 */
	@Column(name = "max_capactiy")
	private int maxCapacity;

	/**
	 * 등록일
	 */
	@Column(name = "date")
	private LocalDateTime date;

	/**
	 * 모든 필드를 초기화하는 생성자
	 *
	 * @param lecture the lecture
	 * @param user the user
	 * @param date the registration date
	 */
	public LectureEnrollment(Long id, Lecture lecture, User user, int maxCapacity, LocalDateTime date) {
		this.id = id;
		this.lecture = lecture;
		this.user = user;
		this.maxCapacity = maxCapacity;
		this.date = date;
	}
}
