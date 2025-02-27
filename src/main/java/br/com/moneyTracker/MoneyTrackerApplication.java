package br.com.moneyTracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
public class MoneyTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoneyTrackerApplication.class, args);
	}

}
