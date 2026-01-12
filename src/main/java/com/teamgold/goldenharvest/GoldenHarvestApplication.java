package com.teamgold.goldenharvest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class GoldenHarvestApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoldenHarvestApplication.class, args);
	}

}
