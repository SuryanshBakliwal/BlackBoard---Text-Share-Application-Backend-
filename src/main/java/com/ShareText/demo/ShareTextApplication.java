package com.ShareText.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShareTextApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShareTextApplication.class, args);
	}

}
