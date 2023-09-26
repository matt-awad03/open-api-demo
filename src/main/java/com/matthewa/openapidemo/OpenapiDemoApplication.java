package com.matthewa.openapidemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @OpenAPIDefinition(info = @Info(title = "Employees API", version = "2.0", description = "Employees Information"))
public class OpenapiDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenapiDemoApplication.class, args);
	}

}
