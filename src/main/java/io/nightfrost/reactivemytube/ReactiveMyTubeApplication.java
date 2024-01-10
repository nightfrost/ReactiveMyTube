package io.nightfrost.reactivemytube;

import io.nightfrost.reactivemytube.configurations.AppConfig;
import io.nightfrost.reactivemytube.configurations.CorsGlobalConfiguration;
import io.nightfrost.reactivemytube.configurations.MongoConfiguration;
import io.nightfrost.reactivemytube.configurations.WebClientConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.web.server.adapter.AbstractReactiveWebInitializer;

@SpringBootApplication
public class ReactiveMyTubeApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveMyTubeApplication.class, args);
	}

}
