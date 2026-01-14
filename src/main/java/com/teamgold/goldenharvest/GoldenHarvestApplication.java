package com.teamgold.goldenharvest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@MapperScan(basePackages = "com.teamgold.goldenharvest.domain.**.mapper")
public class GoldenHarvestApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoldenHarvestApplication.class, args);
	}

}
