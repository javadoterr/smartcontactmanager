package com.javadoterr.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.javadoterr.api.dao.UserRepositoy;
import com.javadoterr.api.entity.User;

@SpringBootApplication
public class SmartcontactmanagerApplication implements CommandLineRunner{
	
	@Autowired
	private UserRepositoy repositoy;
	
	@Autowired
	private BCryptPasswordEncoder encoder;

	public static void main(String[] args) {
		SpringApplication.run(SmartcontactmanagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		//name, email, password, role, enables, imageUrl, about
		User user = new User();
		user.setName("Salman Khan");
		user.setEmail("salman@gmail.com");
		user.setPassword(encoder.encode("123456"));
		user.setRole("ROLE_USER");
		user.setEnables(true);
		user.setImageUrl("default.jpg");
		
		repositoy.save(user);
	}

}
