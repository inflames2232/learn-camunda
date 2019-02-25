package com.learn.validateservice;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;

@SpringBootApplication
@EnableProcessApplication
@Slf4j
public class ValidateserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ValidateserviceApplication.class, args);
	}

	@EventListener
	public void onPostDeploy(PostDeployEvent event) {
		final ArrayList<String> strings = new ArrayList<>();
	}

}

