package com.customer.fileprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootApplication
public class FileProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileProcessingApplication.class, args);

		/*String phoneNumber = "1 555 555 5555";
		String regex = "^(1[-. ]?)?(\\(\\d{3}\\)|\\d{3})[-. ]?\\d{3}[-. ]?\\d{4}$";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(phoneNumber);

		if (matcher.matches()) {
			System.out.println("Phone number is valid");
		} else {
			System.out.println("Phone number is not valid");
		}

		List<Integer> listOfNumbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

		listOfNumbers.parallelStream().forEach(number ->
				System.out.println(number + " " + Thread.currentThread().getName())
		);

		listOfNumbers.stream().forEach(number ->
				System.out.println(number + " ===" + Thread.currentThread().getName())
		);*/
	}

}
