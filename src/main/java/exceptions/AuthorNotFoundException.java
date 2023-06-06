package exceptions;

public class AuthorNotFoundException extends RuntimeException{
	public AuthorNotFoundException(String author) {
		super(String.format("Could not find author with name %s", author));
	}
}
