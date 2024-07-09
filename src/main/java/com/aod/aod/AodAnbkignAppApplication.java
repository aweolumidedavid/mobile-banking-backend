package com.aod.aod;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Java Bank App Learning Tutorial",
				description = "Backend Rest APIs",
				version = "V1.0",
				contact = @Contact(
						name = "Awe Olumide David",
						email = "aweolumidedavid@gmail.com",
						url = "https://github.com/aweolumidedavid/bank_app"
				),
				license = @License(
						name = "The Java Academy",
						url = "https://github.com/aweolumidedavid/bank_app"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Java Bank App Learning Tutorial API Documentation",
				url = "https://github.com/aweolumidedavid/bank_app"
		)
)
public class AodAnbkignAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AodAnbkignAppApplication.class, args);
	}

}
