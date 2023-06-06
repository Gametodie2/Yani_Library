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
import validator.BookValidation;


@Controller
@Slf4j
@RequestMapping("/create")
public class CreateController {
	@Autowired
	private BookRepository repository;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private BookLocationRepository locationRepository;
	@Autowired
    private BookValidation bookValidation;
	
	@GetMapping
	public String create(Model model, Authentication authentication) {
		List<Author> authors = (List<Author>) authorRepository.findAll();
		List<BookLocation> locations = (List<BookLocation>) locationRepository.findAll();

	    User user = userRepo.findByUsername(authentication.getName());
	    model.addAttribute("user", user);
	    model.addAttribute("authorsList", authors);
	    model.addAttribute("locations", locations);
	    model.addAttribute("book", new Book());
	    return "create";
	}
	
	@PostMapping
	public String onSubmit(@Valid Book book, BindingResult result, Model model, Authentication authentication) {		
		
		//validate book
		bookValidation.validate(book, result);
				
		for (BookLocation location : book.getLocations()) {
			if (!location.getLocationName().isEmpty()) {
				location.setBook(book);
			}	
		}
		
		if (result.hasErrors()) {
			log.info("error {}", result);
			
			List<Author> authors = (List<Author>) authorRepository.findAll();
			List<BookLocation> locations = (List<BookLocation>) locationRepository.findAll();

		    User user = userRepo.findByUsername(authentication.getName());
		    model.addAttribute("user", user);
		    model.addAttribute("authorsList", authors);
		    model.addAttribute("locations", locations);   
		    
		    model.addAttribute("book", book);
			return "create";
		}
		log.info("Book Created: {}", book.getTitle());
		
		repository.save(book);
		for (BookLocation location : book.getLocations()) {
			if (!location.getLocationName().isEmpty())
				locationRepository.save(location);
		}
		return "redirect:/catalog";
		
	}
}
