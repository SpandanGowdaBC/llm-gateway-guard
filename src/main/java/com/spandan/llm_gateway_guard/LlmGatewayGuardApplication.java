package com.spandan.llm_gateway_guard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan // CORE: auto-scans @ConfigurationProperties records
public class LlmGatewayGuardApplication {

	public static void main(String[] args) {
		SpringApplication.run(LlmGatewayGuardApplication.class, args);
	}

}
