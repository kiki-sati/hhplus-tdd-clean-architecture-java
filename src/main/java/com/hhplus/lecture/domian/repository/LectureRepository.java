package com.hhplus.lecture.domian.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hhplus.lecture.domian.Lecture;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {

}
