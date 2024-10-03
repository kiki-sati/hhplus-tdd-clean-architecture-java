package com.hhplus.lecture.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hhplus.lecture.domain.Lecture;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

}
