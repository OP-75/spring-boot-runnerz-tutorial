package com.hitesh.runnerz;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import com.hitesh.runnerz.run.Location;
import com.hitesh.runnerz.run.Run;
import com.hitesh.runnerz.run.RunRepository;
import com.hitesh.runnerz.user.User;
import com.hitesh.runnerz.user.UserHttpClient;
import com.hitesh.runnerz.user.UserRestClient;

@SpringBootApplication
public class RunnerzApplication {

	private static final Logger log = LoggerFactory.getLogger(RunnerzApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(RunnerzApplication.class, args);

		System.out.println("Welcome");

	}

	// I think i hate this method for creating rest client
	// UserRestClient seems easier to code instead of UserHttpClient
	@Bean
	UserHttpClient userHttpClient() {
		RestClient restClient = RestClient.create("https://jsonplaceholder.typicode.com");
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(RestClientAdapter.create(restClient))
				.build();
		return factory.createClient(UserHttpClient.class);
	}

	@Bean
	CommandLineRunner runner(RunRepository runRepository, UserRestClient userRestClient) {
		return (args) -> {
			// Run run = new Run(1, "First run", LocalDateTime.now(),
			// LocalDateTime.now().plus(1, ChronoUnit.HOURS), 5,
			// Location.OUTDOOR, 1);
			// runRepository.create(run);

			// List<User> users = userRestClient.findAll();
			// log.info(users.toString());

			// User user = userRestClient.findById(3);
			// log.info(user.toString());

		};
	}

}
