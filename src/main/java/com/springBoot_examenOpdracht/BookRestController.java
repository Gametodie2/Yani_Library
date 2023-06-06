package com.springBoot_examenOpdracht;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import domain.Book;
import repository.BookRepository;

@RestController
@RequestMapping("/api")
public class BookRestController {

		@Autowired
		private BookRepository bookRepository;
		
		@Autowired
	    public BookRestController(BookRepository bookRepository) {
	        this.bookRepository = bookRepository;
	    }
		
		@GetMapping("/books/author/{author}")
	    public List<Book> getBooksByAuthor(@PathVariable("author") String author) {
	        return bookRepository.findBooksByAuthor(author);
	    }
		
		 @GetMapping("/books/isbn/{isbn}")
		    public Book getBookByIsbn(@PathVariable("isbn") String isbn) {
		        return bookRepository.findBookByIsbn(isbn);
		    }
}
