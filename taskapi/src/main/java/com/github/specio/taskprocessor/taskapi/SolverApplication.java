package com.github.specio.taskprocessor.taskapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@SpringBootApplication
public class SolverApplication {

	public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
		SpringApplication.run(SolverApplication.class, args);
	}

}
