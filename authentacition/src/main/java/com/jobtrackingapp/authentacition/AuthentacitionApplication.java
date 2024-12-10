package com.jobtrackingapp.authentacition;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class AuthentacitionApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthentacitionApplication.class, args);
	}


}
