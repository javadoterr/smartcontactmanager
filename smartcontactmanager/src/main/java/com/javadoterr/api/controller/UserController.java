package com.javadoterr.api.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javadoterr.api.dao.UserRepositoy;
import com.javadoterr.api.entity.Contact;
import com.javadoterr.api.entity.User;

@Controller
@RequestMapping(path = "/user")
public class UserController {

	@Autowired
	private UserRepositoy userRepositoy;

	
	//this method execute for every end point of method to add data in model
	@ModelAttribute
	public void addCommonData(Model model, Principal principal) {

		String username = principal.getName();

		// get the user using username(email)
		User user = userRepositoy.getUserByUserName(username);

		System.out.println("USER - " + user);

		model.addAttribute("user", user);

	}

	//dashboard home
	@GetMapping(path = "/index")
	public String dashboard(Model model, Principal principal) {
		
		model.addAttribute("title", "User Dashboard");

		return "normal/user_dashboard";
	}

	// open add form controller
	@GetMapping(path = "/add-contact")
	public String openAddContactForm(Model model) {

		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());

		return "normal/add_contact_form";
	}

}
