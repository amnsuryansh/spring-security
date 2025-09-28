package com.project.springsecsection2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@ComponentScan(basePackages = {"com.project.springsecsection1.controller"})
public class Springsecsection2Application {

	public static void main(String[] args) {
		SpringApplication.run(Springsecsection2Application.class, args);
	}

}
