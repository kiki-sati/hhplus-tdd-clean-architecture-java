package com.hhplus.lecture.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.hhplus.lecture.domain.Lecture;

import jakarta.persistence.LockModeType;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Lecture> findById(Long id);
}
