package com.hhplus.lecture.interfaces.exception;

public enum ErrorMessage {
	LECTURE_NOT_FOUND("강의가 존재 하지 않습니다."),
	INVALID_DATE("신청 강의 날짜가 일치 하지 않습니다."),
	LECTURE_FULL("강의 예약이 꽉 찼습니다."),
	USER_ALREADY_ENROLLED("해당 강의에 이미 등록한 사용자입니다."),
	USER_NOT_FOUND("사용자 정보가 없습니다.");

	private final String message;

	ErrorMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
