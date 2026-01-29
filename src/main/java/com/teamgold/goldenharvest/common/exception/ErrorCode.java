package com.teamgold.goldenharvest.common.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ErrorCode {
    // Common Errors (1000-1999)
    INVALID_REQUEST("1000", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED_ACCESS("1001", "인증 정보가 유효하지 않습니다.", HttpStatus.UNAUTHORIZED), // General unauthorized
    FORBIDDEN_ACCESS("1002", "해당 작업에 대한 권한이 없습니다.", HttpStatus.FORBIDDEN),
    RESOURCE_NOT_FOUND("1003", "요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("1004", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    DUPLICATE_RESOURCE("1005", "이미 존재하는 리소스입니다.", HttpStatus.CONFLICT),
	DUPLICATE_REQUEST("1006", "이미 처리된 요청입니다.", HttpStatus.CONFLICT),

    // User/Auth Errors (2000-2999)
    USER_NOT_FOUND("2000", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXISTS("2001", "이미 가입된 이메일입니다.", HttpStatus.CONFLICT),
    NICKNAME_ALREADY_EXISTS("2002", "이미 존재하는 닉네임입니다.", HttpStatus.CONFLICT),
    PASSWORD_NOT_MATCH("2003", "비밀번호가 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_CREDENTIALS("2004", "이메일 또는 비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED),
    EMAIL_VERIFICATION_REQUIRED("2005", "이메일 인증이 완료되지 않았습니다.", HttpStatus.BAD_REQUEST),
    INVALID_VERIFICATION_CODE("2006", "인증번호가 일치하지 않거나 만료되었습니다.", HttpStatus.BAD_REQUEST),
    REFRESH_TOKEN_INVALID("2007", "유효하지 않은 리프레시 토큰입니다.", HttpStatus.UNAUTHORIZED),
    REFRESH_TOKEN_MISMATCH("2008", "리프레시 토큰이 일치하지 않거나 존재하지 않습니다.", HttpStatus.UNAUTHORIZED),
    PASSWORD_RESET_NOT_ELIGIBLE("2009", "비밀번호 재설정 인증이 필요합니다.", HttpStatus.FORBIDDEN),
    PASSWORD_RESET_ELIGIBILITY_EXPIRED("2010", "비밀번호 재설정 인증 유효 시간이 만료되었습니다.", HttpStatus.FORBIDDEN),
    USER_INACTIVE("2011", "비활성화된 계정입니다.", HttpStatus.FORBIDDEN),
    EMAIL_SEND_FAILED("2012", "이메일 발송에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    PASSWORD_SAME_AS_OLD("2013","기존 비밀번호와 동일합니다.", HttpStatus.FORBIDDEN),
    USER_NOT_APPROVED("2014","승인되지 않은 사용자입니다.", HttpStatus.FORBIDDEN),
    FILE_UPLOAD_ERROR("2015","파일 업로드에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_ONLY_PENDING_CAN_BE_APPROVED("2016","대기 상태인 유저만 승인할 수 있습니다.", HttpStatus.FORBIDDEN),

	// MasterData (3000~3999)
	MASTER_DATA_NOT_FOUND("3000","마스터 데이터를 찾을 수 없습니다.",HttpStatus.NOT_FOUND),

	// Inventory (4000~4999)
	INSUFFICIENT_STOCK("4000", "재고 수량이 부족합니다", HttpStatus.BAD_REQUEST),
	LOT_NOT_FOUND("4001", "Lot 번호에 맞는 재고를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
	INVALID_DISCARD_STATUS("4002", "폐기 상태코드를 잘못되었습니다", HttpStatus.BAD_REQUEST),
	NO_SUCH_SKU("4003", "Sku 번호에 맞는 재고를 찾을 수 없습니다", HttpStatus.NOT_FOUND),

    // Sales (5000-5999)
    ORDER_NOT_FOUND("5000", "주문을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND("5001", "상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    ORDER_STATUS_NOT_FOUND("5002", "주문 상태를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_ORDER_STATUS("5003", "주문 상태를 변경할 수 없습니다.", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_CANCELLED("5004", "이미 취소된 주문입니다.", HttpStatus.BAD_REQUEST),

    //Inquiry (7000-7999)
    INQUIRY_NOT_FOUND("7000", "문의를 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String code;
    private final String message;
    private final HttpStatusCode httpStatusCode;
}
