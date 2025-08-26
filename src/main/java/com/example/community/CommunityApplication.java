package com.example.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
/*
애플리케이션 엔트리, 전역 설정
 */
@EnableJpaAuditing	//BaseEntity의 createdTime/updatedTime 자동 기록 활성화
@SpringBootApplication
public class CommunityApplication {
	public static void main(String[] args) {

		SpringApplication.run(CommunityApplication.class, args);
	}

}