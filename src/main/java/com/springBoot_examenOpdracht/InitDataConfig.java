package com.springBoot_examenOpdracht;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import domain.Author;
import domain.Book;
import domain.BookLocation;
import domain.Role;
import domain.User;
import lombok.extern.slf4j.Slf4j;
import repository.AuthorRepository;
import repository.BookLocationRepository;
import repository.BookRepository;
import repository.UserRepository;

@Slf4j
@Component
public class InitDataConfig implements CommandLineRunner{
	
	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private AuthorRepository authorRepository;
	
	@Autowired
	private BookLocationRepository locationRepository;
	
	@Autowired
	private UserRepository userRepository;

	
	private Book mockingbird;
	private Book book1984;
	private Book greatGatsby;
	private Book theAlchemist;
	private Book harryPotter;
	private Book silentPatient;
	private Book cProgramming;

	BookLocation mockingbirdLoc1 = new BookLocation("ABC123", "DEF456", "New York");
	BookLocation mockingbirdLoc2 = new BookLocation("GHI789", "JKL012", "Los Angeles");

	BookLocation book1984Loc1 = new BookLocation("MNO345", "PQR678", "London");
	BookLocation book1984Loc2 = new BookLocation("STU901", "VWX234", "Paris");

	BookLocation greatGatsbyLoc1 = new BookLocation("YZA567", "BCD890", "Chicago");
	BookLocation greatGatsbyLoc2 = new BookLocation("EFG123", "HIJ456", "San Francisco");

	BookLocation theAlchemistLoc1 = new BookLocation("KLM789", "NOP012", "Rio de Janeiro");
	BookLocation theAlchemistLoc2 = new BookLocation("QRS345", "TUV678", "Lisbon");

	BookLocation harryPotterLoc1 = new BookLocation("WXY901", "ZAB234", "Edinburgh");
	BookLocation harryPotterLoc2 = new BookLocation("CDE567", "FGH890", "Brussels");

	BookLocation silentPatientLoc1 = new BookLocation("IJK123", "LMN456", "Seoul");
	BookLocation silentPatientLoc2 = new BookLocation("OPQ789", "RST012", "Berlin");

	BookLocation cProgrammingLoc1 = new BookLocation("UVW345", "XYZ678", "Tokyo");
	BookLocation cProgrammingLoc2 = new BookLocation("ABC901", "DEF234", "Sydney");

	@Override
	public void run(String... args) throws Exception {
		
		createBookWithLocations("The C Programming Language", List.of("Brian W. Kernighan", "Dennis M. Ritchie"), "https://pictures.abebooks.com/isbn/9780876925225-us.jpg", "9783161484100", 9.99, cProgrammingLoc1, cProgrammingLoc2);

		createBookWithLocations("To Kill a Mockingbird", List.of("Harper Lee"), "https://cdn.britannica.com/21/182021-050-666DB6B1/book-cover-To-Kill-a-Mockingbird-many-1961.jpg", "9781455586691", 20, mockingbirdLoc1, mockingbirdLoc2);

		createBookWithLocations("1984", List.of("George Orwell"), "https://kbimages1-a.akamaihd.net/a5312ed2-bc80-4f4c-972b-c24dc5990bd5/1200/1200/False/george-orwell-1984-4.jpg", "9780804139021", 15, book1984Loc1, book1984Loc2);

		createBookWithLocations("The Great Gatsby", List.of("F. Scott Fitzgerald"), "https://kbimages1-a.akamaihd.net/c5742da9-e63f-4ed5-acb6-074fab5e3f41/1200/1200/False/the-great-gatsby-11.jpg", "9780143124542", 18, greatGatsbyLoc1, greatGatsbyLoc2);

		createBookWithLocations("The Alchemist", List.of("Paulo Coelho"), "https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1654371463i/18144590.jpg", "9780765326358", 25, theAlchemistLoc1, theAlchemistLoc2);

		createBookWithLocations("Harry Potter and the Philosopher's Stone", List.of("J.K. Rowling"), "https://static.posters.cz/image/750/harry-potter-philosopher-s-stone-i104639.jpg", "9780062409850", 30, harryPotterLoc1, harryPotterLoc2);

		createBookWithLocations("The Silent Patient", List.of("Alex Michaelides"), "https://m.media-amazon.com/images/I/91lslnZ-btL._AC_UF1000,1000_QL80_.jpg", "9780307277679", 15, silentPatientLoc1, silentPatientLoc2);

	    
	 // Create a new user
	    User user = new User("john.doe",  BCrypt.hashpw("password", BCrypt.gensalt()), Role.ADMIN, "https://media4.giphy.com/media/u1Wk7G3XhThehz3vM7/200w.gif?cid=6c09b9527k75gvo1oi7plhy510nmxzcqeac02okqfv8ftn14&ep=v1_gifs_search&rid=200w.gif&ct=g", 3); // Set the user role
	    User customer = new User("joanna.doe",  BCrypt.hashpw("password", BCrypt.gensalt()), Role.USER, "https://media2.giphy.com/media/gzoOh4SkStpZQxZTzG/giphy.gif", 2); // Set the user role
	    // Save the user to the database
	    userRepository.save(user);
	    userRepository.save(customer);
	}
	
	
	private Book createBook(String title, List<String> authorNames, String image, String isbn, double price) {
        Book book = new Book(title, image, isbn, price);
        List<Author> authors = new ArrayList<>();
        for (String authorName : authorNames) {
            Author author = new Author(authorName);
            author.addBook(book);
            authors.add(author);
        }
        book.setAuthors(authors);
        bookRepository.save(book);
        return book;
    }
	
	private void createBookWithLocations(String title, List<String> authorNames, String image, String isbn, double price, BookLocation location1, BookLocation location2) {
	    Book book = createBook(title, authorNames, image, isbn, price);
	    List<BookLocation> locations = new ArrayList<>();
	    locations.add(location1);
	    locations.add(location2);

	    book.setLocations(locations);
	    bookRepository.save(book);
	    
	    location1.setBook(book);
	    location2.setBook(book);
	    
	    locationRepository.save(location1);
	    locationRepository.save(location2);
	    
	    log.info("Created book: {}", book.getTitle());
	}

}
