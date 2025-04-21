package com.lfpsys.lfpsys_tax_calculation_services;

import static java.lang.System.getenv;

import java.util.Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LfpsysTaxCalculationServicesApplication {

	private static final String DB_URL = "DB_URL";
	private static final String DB_USERNAME = "DB_USERNAME";
	private static final String DB_PASSWORD = "DB_PASSWORD";
	public static final String KAFKA_BOOTSTRAP_SERVERS = "KAFKA_BOOTSTRAP_SERVERS";
	private static final String DB_CACHE_REDIS_URL = "DB_CACHE_REDIS_URL";
	private static final String DB_CACHE_REDIS_PORT = "DB_CACHE_REDIS_PORT";
	private static final String DB_CACHE_REDIS_PASSWORD = "DB_CACHE_REDIS_PASSWORD";
	private static final String SERVER_PORT = "SERVER_PORT";

	public static void main(String[] args) {
		var application = new SpringApplication(LfpsysTaxCalculationServicesApplication.class);
		application.setDefaultProperties(getPropertiesFromEnvironments());
		application.run(args);
	}

	private static Properties getPropertiesFromEnvironments() {
		var properties = new Properties();

		//Application
		properties.put("server.port", getenv(SERVER_PORT));

		//DB connection
		properties.put("spring.datasource.driver-class-name", "org.postgresql.Driver");
		properties.put("spring.jpa.hibernate.ddl-auto", "none");
		properties.put("spring.jpa.show-sql", false);
		properties.put("spring.jpa.open-in-view", false);
		properties.put("spring.datasource.url", getenv(DB_URL));
		properties.put("spring.datasource.username", getenv(DB_USERNAME));
		properties.put("spring.datasource.password", getenv(DB_PASSWORD));

		//Kafka
		properties.put("spring.kafka.consumer.group-id", "group_id");
		properties.put("spring.kafka.consumer.auto-offset-reset", "earliest");
		properties.put("spring.kafka.consumer.enable-auto-commit", true);
		properties.put("spring.kafka.producer.key-serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("spring.kafka.producer.value-serializer", "org.apache.kafka.common.serialization.StringSerializer");
		properties.put("spring.kafka.bootstrap-servers", getenv(KAFKA_BOOTSTRAP_SERVERS));

		// Redis
		properties.put("spring.data.redis.host", getenv(DB_CACHE_REDIS_URL));
		properties.put("spring.data.redis.port", getenv(DB_CACHE_REDIS_PORT));
		properties.put("spring.data.redis.password", getenv(DB_CACHE_REDIS_PASSWORD));

		return properties;
	}
}
