package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void configure_SuccessfulInitialization() {
		ServletInitializer servletInitializer = new ServletInitializer();
		SpringApplicationBuilder applicationBuilder = new SpringApplicationBuilder();

		servletInitializer.configure(applicationBuilder);
	}

}
