package com.backend.xplaza;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class XplazaApplication {

	public static void main(String[] args) {
		SpringApplication.run(XplazaApplication.class, args);
	}

}
