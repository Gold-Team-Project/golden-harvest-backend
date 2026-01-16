package com.teamgold.goldenharvest;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@MapperScan("com.teamgold.goldenharvest.**.mapper")
@SpringBootApplication
public class GoldenHarvestApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoldenHarvestApplication.class, args);
	}

}
