// 이 파일이 Spring Boot 프로젝트의 시작점임 (메인 클래스)
package com.mysite.sbb;
  // 네 프로젝트 패키지 이름으로 바꿔!!

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // 이 어노테이션이 제일 중요! Spring Boot 켜짐
public class SbbApplication {

    public static void main(String[] args) {
        // 여기서 프로그램 시작
        SpringApplication.run(SbbApplication.class, args);
    }
}