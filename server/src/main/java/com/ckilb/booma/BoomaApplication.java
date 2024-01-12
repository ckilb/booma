package com.ckilb.booma;

import com.ckilb.booma.configuration.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class BoomaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoomaApplication.class, args);
	}

}
