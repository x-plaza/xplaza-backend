package com.backend.xplaza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@EnableAsync
@SpringBootApplication
@EnableAutoConfiguration
public class XplazaApplication {

	@PostConstruct
	public void init(){
		// Setting Spring Boot SetTimeZone
		TimeZone.setDefault(TimeZone.getTimeZone("SAST"));
	}

	public static void main(String[] args) {
		SpringApplication.run(XplazaApplication.class, args);
	}

}
