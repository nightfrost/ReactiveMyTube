package io.nightfrost.reactivemytube;

import io.swagger.v3.oas.models.info.Info;
import lombok.Value;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class ReactiveMyTubeApplication {

	@Autowired
	private Environment environment;

	public static void main(String[] args) {
		SpringApplication.run(ReactiveMyTubeApplication.class, args);
	}

	@Bean
	public GroupedOpenApi employeesOpenApi() {
		String[] paths = { "/api/v1/movies/**" };
		return GroupedOpenApi.builder().group("movies")
				.addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Movies API").version(environment.getProperty("springdoc.version"))))
				.pathsToMatch(paths)
				.build();
	}

	@Bean
	public GroupedOpenApi userOpenApi() {
		String[] paths = { "/api/v1/users/**" };
		return GroupedOpenApi.builder().group("users")
				.addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Users API").version(environment.getProperty("springdoc.version"))))
				.pathsToMatch(paths)
				.build();
	}

	@Bean
	public GroupedOpenApi coffeeOpenApi() {
		String[] paths = { "/api/v1/comments/**" };
		return GroupedOpenApi.builder().group("comments")
				.addOpenApiCustomizer(openApi -> openApi.info(new Info().title("Comments API").version(environment.getProperty("springdoc.version"))))
				.pathsToMatch(paths)
				.build();
	}

}
