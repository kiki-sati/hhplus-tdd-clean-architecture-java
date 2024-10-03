package com.hhplus.lecture.application;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hhplus.lecture.domain.Lecture;
import com.hhplus.lecture.domain.LectureEnrollment;
import com.hhplus.lecture.domain.repository.LectureEnrollmentRepository;
import com.hhplus.lecture.domain.repository.LectureRepository;
import com.hhplus.lecture.interfaces.exception.ErrorMessage;

import lombok.RequiredArgsConstructor;

/**
 * The type Lecture service.
 */
@Service
@RequiredArgsConstructor
public class LectureService {

	private final LectureRepository lectureRepository;
	private final LectureEnrollmentRepository lectureEnrollmentRepository;

	/**
	 * 강의 신청
	 * @param lectureId the lecture id
	 * @param userId the user id
	 * @param requestedDate the requested date
	 * @return string
	 */
	@Transactional
	public String enrollInLecture(Long lectureId, Long userId, LocalDate requestedDate) {

		// 강의 존재 확인
		Lecture lecture = lectureRepository.findById(lectureId)
			.orElseThrow(() -> new IllegalArgumentException(ErrorMessage.LECTURE_NOT_FOUND.getMessage()));

		// 강의 날짜 확인
		if (!lecture.getDate().isEqual(requestedDate)) {
			throw new IllegalArgumentException(ErrorMessage.INVALID_DATE.getMessage());
		}

		// 현재 등록된 사용자 수 확인
		int currentEnrollmentCount = lectureEnrollmentRepository.findByLectureId(lectureId).size();

		// 최대 수용 인원 초과 여부 확인
		if (currentEnrollmentCount >= lecture.getMaxCapacity()) {
			throw new IllegalStateException(ErrorMessage.LECTURE_FULL.getMessage());
		}

		// 중복 신청 여부 확인
		if (lectureEnrollmentRepository.existsByLectureIdAndUserId(lectureId, userId)) {
			throw new IllegalArgumentException(ErrorMessage.USER_ALREADY_ENROLLED.getMessage());
		}

		// 강의 등록
		LectureEnrollment enrollment = LectureEnrollment.create(lectureId, userId, LocalDate.now());
		lectureEnrollmentRepository.save(enrollment);

		return "강의 등록 성공";
	}

	/**
	 * 날짜별로 현재 신청 가능한 특강 목록을 조회
	 *
	 * @return the available lectures
	 */
	public List<Lecture> getOpenLectures() {
		return lectureRepository.findAll();
	}

	/**
	 * 특강 신청 완료 목록 조회 API
	 *
	 * @param userId the user id
	 * @return the enrollments by user id
	 */
	public List<LectureEnrollment> getEnrollmentsByUserId(Long userId) {
		return lectureEnrollmentRepository.findByUserId(userId);
	}
}
