package com.hhplus.lecture.domian;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 정보
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "`user`") // 테이블 이름을 백틱으로 감싸 예약어 회피
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * 사용자 이름
	 */
	private String name;

	/**
	 * 사용자 이메일
	 */
	private String email;

	/**
	 * 등록 일자
	 */
	@Column(name = "registration_date")
	private LocalDateTime registrationDate;

	public User(Long id, String name, String email, LocalDateTime registrationDate) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.registrationDate = registrationDate;
	}
}
