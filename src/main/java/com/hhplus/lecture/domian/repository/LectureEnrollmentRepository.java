package com.hhplus.lecture.domian.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hhplus.lecture.domian.LectureEnrollment;

@Repository
public interface LectureEnrollmentRepository extends JpaRepository<LectureEnrollment, Long> {
	List<LectureEnrollment> findByLectureId(Long lectureId);

	boolean existsByLectureIdAndUserId(Long lectureId, Long userId);

	List<LectureEnrollment> findByUserId(Long userId);
}
