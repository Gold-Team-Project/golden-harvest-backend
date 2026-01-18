package com.teamgold.goldenharvest.domain.inventory.command.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
* Native key 생성을 위한 고유한 숫자값을 생성하는 DB 객체
* 1부터 1씩 증가하는 숫자값을 고유하게 반환 가능
* */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class IdGenerator {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inv_gen")
	@SequenceGenerator(
		name = "inv_gen",
		sequenceName = "INVENTORY_SEQ_OBJ",
		initialValue = 1,
		allocationSize = 50
	)
	private Long id;

	public static String createId(String type) {
		IdGenerator generator = new IdGenerator();
		Long sequenceNum = generator.getId();

		String formattedType = type.substring(0, Math.min(3, type.length())).toUpperCase();
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String formattedSeq = String.format("%06d", sequenceNum);

		// TYPE_YYYYMMDD_NNNNNN 형식의 native id 생성
		return formattedType + "_" + today + "_" + formattedSeq;
	}
}
