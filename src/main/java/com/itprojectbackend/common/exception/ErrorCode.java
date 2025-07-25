package com.itprojectbackend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

	//Internal Server Error
	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제가 생겼습니다."),

	// Client Error
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "적절하지 않은 HTTP 메소드입니다."),
	INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, "요청 값의 타입이 잘못되었습니다."),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "적절하지 않은 값입니다."),
	NOT_FOUND(HttpStatus.NOT_FOUND, "해당 리소스를 찾을 수 없습니다."),
	MISSING_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST, "필수 파라미터가 누락되었습니다."),

	// User Error
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자의 정보를 찾을 수 없습니다."),
	EMAIL_ALREADY_USED(HttpStatus.BAD_REQUEST,"이미 존재하는 이메일입니다."),
	INVALID_ENUM(HttpStatus.BAD_REQUEST, "알 수 없는 항공사입니다."),
	PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST,"비밀번호가 일치하지 않습니다"),
	INVALID_PASSWORD_LENGTH(HttpStatus.BAD_REQUEST, "비밀번호는 6자 이상이여야 합니다."),
	PASSWORD_ALREADY_USED(HttpStatus.BAD_REQUEST,"사용중인 비밀번호 입니다."),
	NICKNAME_ALREADY_USED(HttpStatus.BAD_REQUEST," 사용중인 닉네임 입니다."),
	INVALID_NICKNAME_LENGTH(HttpStatus.BAD_REQUEST,"닉네임은 2자 이상이여야 합니다."),



	//Airport Error
	INVALID_AIRPORT(HttpStatus.BAD_REQUEST,"해당 공항은 존재하지 않습니다."),

	//Crew Error
	FLIGHT_SCHEDULE_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 비행 스케줄입니다."),
	CREW_SCHEDULE_NOT_FOUND(HttpStatus.BAD_REQUEST,"존재하지 않는 비행 스케줄입니다."),
	UNAUTHORIZED_CREW_SCHEDULE_ACCESS(HttpStatus.UNAUTHORIZED,"해당 유저의 비행 스케줄이 아닙니다."),

	//Diary Error
	DIARY_NOT_FOUND(HttpStatus.BAD_REQUEST,"존재하지 않는 비행 일기 입니다."),
	UNAUTHORIZED_DIARY_ACCESS(HttpStatus.UNAUTHORIZED,"해당 유저의 비행 일기가 아닙니다");

    private final HttpStatus status;
	private final String message;

	ErrorCode(HttpStatus status, String message) {
		this.status = status;
		this.message = message;
	}
}
