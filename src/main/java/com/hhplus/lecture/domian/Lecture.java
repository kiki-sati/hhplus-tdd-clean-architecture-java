package com.hhplus.lecture.domian;

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
	private Long id;

	/**
	 * 	특강 이름
	 */
	private String name;

	/**
	 * 강사 이름
	 */
	private String lecturerName;

	/**
	 * 등록된 사용자 ID 목록
	 */
	@ElementCollection
	private Set<Long> registeredUsers = new HashSet<>();

	/**
	 * 모든 필드를 초기화하는 생성자 (setter 사용을 안하기 위해 사용)
	 *
	 * @param id the id
	 * @param name the name
	 * @param lecturerName the lecturer name
	 * @param maxCapacity the max capacity
	 */
	public Lecture(Long id, String name, String lecturerName) {
		this.id = id;
		this.name = name;
		this.lecturerName = lecturerName;
		this.registeredUsers = new HashSet<>();
	}

	/**
	 * 등록된 사용자 추가 메서드
	 *
	 * @param userId the user id
	 */
	public void addRegisteredUser(Long userId) {
		registeredUsers.add(userId);
	}
}
