package io.nightfrost.reactivemytube.configurations;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan
@PropertySource(value = "classpath:application.properties", ignoreResourceNotFound = true)
public class AppConfig {

    //TODO: Setup dynamic application.properties for MongoConfiguration, WebClientConfiguration and so on...

}
