package com.emrecosar.ci;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class CIApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(CIApplication.class).logStartupInfo(false).run(args);
	}
}
