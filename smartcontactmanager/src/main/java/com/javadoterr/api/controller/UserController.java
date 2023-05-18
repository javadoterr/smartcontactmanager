package com.javadoterr.api.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.javadoterr.api.dao.ContactRepository;
import com.javadoterr.api.dao.UserRepositoy;
import com.javadoterr.api.entity.Contact;
import com.javadoterr.api.entity.User;
import com.javadoterr.api.helper.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping(path = "/user")
public class UserController {

	@Autowired
	private UserRepositoy userRepositoy;
	
	@Autowired
	private ContactRepository contactRepository;

	
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
	
	//handler for processing the contact form
	@PostMapping(path = "/process-contact")
	public String processContact(@ModelAttribute Contact contact,
//												@RequestParam MultipartFile file,
												Principal principal,
												HttpSession session) {
		
		System.out.println("processContact method called....");
		
		try {
		
		String name = principal.getName();
		User user = this.userRepositoy.getUserByUserName(name);
		
		
		//processing and uploading file
//		if(file.isEmpty()) {
//			//if the file is empty then try our message
//			System.out.println("File is emplty !");
//		}else {
//			//upload the file to folder and update the name to contact
//			String originalFilename = file.getOriginalFilename();
//			System.out.println("File - "+ originalFilename);
//			contact.setImage(file.getOriginalFilename());
//			
//			//save the image in image folder of static
//			File saveFileLocation = new ClassPathResource("static/img").getFile();
//			
//			//creating the path to save the image 
//			Path path = Paths.get(saveFileLocation.getAbsolutePath()+File.separator+file.getOriginalFilename());
//			
//			Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
//			
//			System.out.println("Image is uploaded !");
//			
//			
//		}
		
		contact.setImage("contact.png");
		contact.setUser(user);
		
		user.getContacts().add(contact);
		
		
		
		
		this.userRepositoy.save(user);
		
		System.out.println(contact);
		
		System.out.println("Added to data base"); 
		
		//message success
		session.setAttribute("message", new Message("Your contact is added !! Add More...", "success"));
		
		
		}catch(Exception e) {
			System.out.println("ERROR "+ e.getMessage());
			e.printStackTrace();
			//message error
			session.setAttribute("message", new Message("Something went wrong !! try again...", "danger"));
		}
		
		return "normal/add_contact_form";
	}
	
	//show contacts handler
	//per page = 3[n]
	//current page = 0[page]
	@GetMapping(path = "/show-contacts/{page}")
	public String showContacts(@PathVariable("page") Integer page,Model model, Principal principal) {
		
		model.addAttribute("title", "Show User Contacts");
		
		String nameName = principal.getName();
		User savedUser = this.userRepositoy.getUserByUserName(nameName);
		
		PageRequest pageRequest = PageRequest.of(page, 3);
		Page<Contact> contacts = this.contactRepository.findContactsByUser(savedUser.getId(), pageRequest);
		
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		
		
		return "normal/show_contacts";
	}
	
	
	//showing particular contact details
	@GetMapping(path = "/contact/{cId}")
	public String showContactDetail(@PathVariable("cId")Integer cId, Model model, Principal principal) {
		
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		
		String name = principal.getName();
		User user = this.userRepositoy.getUserByUserName(name);
		
		if(user.getId() == contact.getUser().getId()) {			
			model.addAttribute("contact", contact);
			model.addAttribute("title", contact.getName());
		}
		
		return "normal/contact_detail";
	}
	
	//delete contact by id handler
	@GetMapping(path = "/delete/{cId}")
	public String deleteContact(@PathVariable("cId") Integer cId, Model model, HttpSession session) {
		Optional<Contact> savedContactOptional = this.contactRepository.findById(cId);
		Contact contact = savedContactOptional.get();
		
		//detaching contact and user
		contact.setUser(null);
		
		
		this.contactRepository.delete(contact);
		
		
		session.setAttribute("message", new Message("Contact deleted successfully....", "success"));
		
		return "redirect:/user/show-contacts/0";
	}
	
	
	//open update form handler
	@PostMapping(path = "/open-contact/{cId}")
	public String updateForm(@PathVariable("cId") Integer cId, Model model) {
		
		model.addAttribute("title", "Update Content");
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		Contact contact = contactOptional.get();
		
		model.addAttribute("contact", contact);
		return "normal/update_form";
	}
	
	
	//update contact handler
	@PostMapping(path = "/process-update")
	public String updateHandler(@ModelAttribute Contact contact, 
											Model model, HttpSession session, Principal principal) {
		
		User user = this.userRepositoy.getUserByUserName(principal.getName());
		contact.setUser(user);
		contact.setImage("contact.png");
		this.contactRepository.save(contact);
		
		session.setAttribute("message", new Message("Your contact is updated...", "success"));
		
		
		return "redirect:/user/show-contacts/0";
	}
	
	
	
	
	
	
	

}
