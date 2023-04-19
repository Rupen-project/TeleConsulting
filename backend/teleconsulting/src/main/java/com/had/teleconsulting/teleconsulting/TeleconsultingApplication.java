package com.had.teleconsulting.teleconsulting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class TeleconsultingApplication {

	public static void main(String[] args) {
		SpringApplication.run(TeleconsultingApplication.class, args);
	}

}
