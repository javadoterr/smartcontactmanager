package com.javadoterr.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javadoterr.api.dao.UserRepositoy;
import com.javadoterr.api.entity.User;
import com.javadoterr.api.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepositoy userRepositoy;
	
	@GetMapping(path = "/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}
	
	@GetMapping(path = "/about")
	public String about(Model model) {
		model.addAttribute("title", "About - Smart Contact Manager");
		return "about";
	}
	
	@GetMapping(path = "/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register - Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
//	handler for registering user
	@PostMapping(path = "/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
										Model model, HttpSession session) {
		
		
		try {
			
			//agreement is not working
			/* 
			 * if(!agreement) {
			 * System.out.println("You have not agreed the terms and conditions !"); throw
			 * new Exception("You have not agreed the terms and conditions !"); }
			 */
			if(result.hasErrors()) {
				result.getAllErrors().forEach(err -> System.out.println("ERROR -> "+ err.getDefaultMessage()));
				model.addAttribute("user", user);
				return "signup";
			}
			
			user.setRole("ROLE_USER");
			user.setEnables(true);
			user.setImageUrl("default.jpg");
			
			User savedUser = userRepositoy.save(user);
			
			
			
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Successfully Registered !", "alert-success"));
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went worng !"+e.getMessage(), "alert-danger"));
			return "signup";
		}
		return "signup";
	}

}
