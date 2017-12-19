package com.hxiloj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"com.hxiloj"})
public class SpringSecurityOauth2ServerLdapApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSecurityOauth2ServerLdapApplication.class, args);
	}
}
