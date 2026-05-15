package com.frodo.architect;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class ArchitectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ArchitectApplication.class, args);
	}

	// TODO: REMOVE BEFORE PROD - startup smoke runner
	@Profile("!test")
	@Bean
	public ApplicationRunner runner() {
		return args -> {

			RestClient client = RestClient.create();

			String response = client.get()
					.uri("http://localhost:8080/hello")
					.retrieve()
					.body(String.class);

			System.out.println("🔥🔥🔥🔥🔥🔥🔥 Response from /hello: " + response + "🔥🔥🔥🔥🔥 ");
		};
	}

}
