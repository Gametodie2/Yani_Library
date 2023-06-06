package com.springBoot_examenOpdracht;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import domain.Author;
import domain.Book;
import domain.BookLocation;
import domain.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import repository.AuthorRepository;
import repository.BookLocationRepository;
import repository.BookRepository;
import repository.UserRepository;
import validator.AuthorValidation;
import validator.BookValidation;

@Controller
@Slf4j
@RequestMapping("/author")
public class AuthorController {
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
    private AuthorValidation authorValidation;
	
	@GetMapping
	public String create(Model model, Authentication authentication) {
	    User user = userRepo.findByUsername(authentication.getName());
	    model.addAttribute("user", user);

	    model.addAttribute("author", new Author());
	    return "author";
	}
	
	@PostMapping
	public String onSubmit(@Valid Author author, BindingResult result, Model model, Authentication authentication) {		
		
		//validate book
		authorValidation.validate(author, result);
				
		
		if (result.hasErrors()) {
			log.info("error {}", result);
		    User user = userRepo.findByUsername(authentication.getName());
		    model.addAttribute("user", user);  
		    
		    model.addAttribute("author", author);
			return "author";
		}
		log.info("Author Created: {}", author.getName());
		
		authorRepository.save(author);
		return "redirect:/catalog";
		
	}
}
