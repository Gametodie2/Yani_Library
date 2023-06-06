package com.springBoot_examenOpdracht;

import domain.Author;
import domain.Book;
import domain.BookLocation;
import domain.User;
import lombok.extern.slf4j.Slf4j;
import repository.AuthorRepository;
import repository.BookLocationRepository;
import repository.BookRepository;
import repository.UserRepository;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@Slf4j
@RequestMapping("/catalog")
public class BookController {
	
	@Autowired
	private BookRepository repository;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private BookLocationRepository locationRepository;

	
	@GetMapping("/book/{id}")
	public String book(@PathVariable String id, Model model, Authentication authentication) {
		Book book = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid book id: " + id));
		User user = userRepo.findByUsername(authentication.getName());
		model.addAttribute("user", user);
	    model.addAttribute("book", book);
	    
	    
	    return "BookDetails";
	}
	@PostMapping("/{id}/favorite")
	public String addToFavorites(@PathVariable("id") String id, Model model, Authentication authentication,
            RedirectAttributes redirectAttributes) {
	    User user = userRepo.findByUsername(authentication.getName());
	    Book book = repository.findById(id).orElse(null);

	    if (book == null) {
            return "error";
        }
	    if (user.getBooks().contains(book)) {
	    	user.removeFavoriteBook(book);
            redirectAttributes.addFlashAttribute("conf", "removed");
            redirectAttributes.addFlashAttribute("favobook", book.getTitle());
        }else {
        	try {
        		user.addFavoriteBook(book);
        	} catch (Exception e) {
        		System.out.println(e.getMessage());
        	}   
            redirectAttributes.addFlashAttribute("conf", "added");
            redirectAttributes.addFlashAttribute("favobook", book.getTitle());
        }
	    userRepo.save(user);
	    return "redirect:/catalog";
	}
	
	@GetMapping("/most-popular")
	public String getMostLikedBooks(Model model, Authentication authentication) {
		List<Book> mostLikedBooks = repository.findMostLikedBooks();
	     // Sort books in descending order based on likes and then title
	    model.addAttribute("books", mostLikedBooks);
	    
	    User user = userRepo.findByUsername(authentication.getName());
	    model.addAttribute("user", user);
	    return "most_liked_books";
	}
	
	@GetMapping
	public String home(Model model, Authentication authentication) {
		List<Book> books = (List<Book>) repository.findAll();
		User user = userRepo.findByUsername(authentication.getName());
		model.addAttribute("books", books);
		model.addAttribute("user", user);
		return "Catalog";
	}
}

