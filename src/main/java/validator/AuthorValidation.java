package validator;

import org.springframework.beans.factory.annotation.Autowired;

import domain.Author;
import domain.Book;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import repository.AuthorRepository;

public class AuthorValidation implements Validator{

	@Autowired
	private AuthorRepository authorRepository;
	
	@Override
	public void validate(Object target, Errors errors) {
		Author author = (Author) target;
		
		 // Validate ISBN
	    String name = author.getName();
	    if (!isValidName(name)) {
            errors.rejectValue("name", "invalidFormat.author.name", "Name can only contain letters.");
        }
		
	}
	
	private boolean isValidName(String name) {
		return name.matches("[a-zA-Z\\s]+");
    }
	@Override
	public boolean supports(Class<?> clazz) {
		return Author.class.isAssignableFrom(clazz);
	}
}
