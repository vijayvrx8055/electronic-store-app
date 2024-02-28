package com.vrx.electronic.store;

import com.vrx.electronic.store.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class ElectrohomesServiceAppApplication {//implements CommandLineRunner {

//	@Autowired
//	private PasswordEncoder passwordEncoder;

	public static void main(String[] args)
	{
		SpringApplication.run(ElectrohomesServiceAppApplication.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		System.out.println(passwordEncoder.encode("abcd1234"));
//	}
}
