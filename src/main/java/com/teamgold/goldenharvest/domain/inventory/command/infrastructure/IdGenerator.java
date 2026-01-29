package com.teamgold.goldenharvest.domain.inventory.command.infrastructure;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IdGenerator {
	/**
	 * @param type 상품 타입 (예: APPLE)
	 * @return TYPE_YYYYMMDD_NNNNNN 형식의 ID
	 */
	public static String createId(String type) {
		String sequenceStr = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();

		String formattedType = type.substring(0, Math.min(3, type.length())).toUpperCase();
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		// TYPE_YYYYMMDD_NNNNNN 형식의 native id 생성
		return formattedType + "_" + today + "_" + sequenceStr;
	}
}
