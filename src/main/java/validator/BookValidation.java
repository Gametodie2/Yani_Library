package validator;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

import com.springBoot_examenOpdracht.BookController;

import domain.Author;
import domain.Book;
import domain.BookLocation;
import jakarta.servlet.Registration;
import lombok.extern.slf4j.Slf4j;
import repository.BookLocationRepository;
import repository.BookRepository;

@Slf4j
public class BookValidation implements Validator{
	
	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private BookLocationRepository locationRepository;

	@Override
	public void validate(Object target, Errors errors) {
	    Book book = (Book) target;

	    // Validate ISBN
	    String isbn = book.getIsbn();
	    validateIsbn(isbn, errors);

	    // Validate Price
	    double price = book.getPrice();
	    validatePrice(price, errors);
	    

	    // Validate URL format
	    String url = book.getImage();
	    if (!StringUtils.isEmpty(url)) {
	        if (!isValidURL(url)) {
	            errors.rejectValue("image", "invalidFormat.book.image", "Invalid URL format.");
	        }
	    }
	    
	    validateLocations(book, errors);
	    
	 // Validate if all selected authors are different
	    List<Author> authors = book.getAuthors();
	    if (!isAllAuthorsDifferent(authors)) {
	    	errors.rejectValue("authors", "duplicateAuthors", "All selected authors must be different.");
	    }
	}
	
	private boolean isAllAuthorsDifferent(List<Author> authors) {
	    Set<String> authorNames = new HashSet<>();
	    for (Author author : authors) {
	    	if (author != null) {
		        String authorName = author.getName();
		        if (authorNames.contains(authorName)) {
		            return false;
		        }
		        authorNames.add(authorName);
	    	}
	    }
	    return true;
	}
	
	private void validateIsbn(String isbn, Errors errors) {
		if (isbn.length() != 13) {
	    	Integer [] param = { 13 };
	        errors.rejectValue("isbn", "lenghtOfIsbn.book.isbn" ,param, "ISBN number must be {0} numbers long.");
	    } else if (bookRepository.existsByIsbn(isbn)) {
	        errors.rejectValue("isbn", "duplicateIsbn.book.isbn", "ISBN number already exists in the database.");
	    } else {
	        // ISBN checksum validation
	        int sum = 0;
	        for (int i = 0; i < isbn.length() - 1; i++) {
	            int digit = Character.getNumericValue(isbn.charAt(i));
	            if (i % 2 == 0) {
	                sum += digit;
	            } else {
	                sum += digit * 3;
	            }
	        }

	        int checkDigit = (10 - (sum % 10)) % 10;
	        if (checkDigit != Character.getNumericValue(isbn.charAt(12))) {
	            errors.rejectValue("isbn", "invalidChecksum.book.isbn", "Invalid ISBN checksum.");
	        }
	    }
	}
	
	private void validatePrice(double price, Errors errors) {
		if (Double.isNaN(price)) {
	        errors.rejectValue("price", "missingValue.book.price", "Price is required.");
	    } else if (price <= 0 || price >= 100) {
	    	int minPrice = 0;
	        int maxPrice = 100;
	        Integer[] params = { minPrice, maxPrice };
	        errors.rejectValue("price", "invalidRange.book.price", params, "Price must be greater than {0} and less than {1} (exclusive).");
	    }
	}
	
	private void validateLocations(Book book, Errors errors) {
        List<BookLocation> locations = book.getLocations();

        for (int i = 0; i < locations.size(); i++) {
            BookLocation location = locations.get(i);
            if (location != null) {
                if (!location.getLocationName().isEmpty()) {
                    log.info("Location {}", location.getLocationName());

                    // Perform validation for the non-null and non-empty location
                    // Add your validation logic here
                    String locationCode1 = location.getLocationCode1();
		            String locationCode2 = location.getLocationCode2();
		            String locationName = location.getLocationName();
		            
		            Integer[] codes = {50, 300, 50};
		
		            // Validate locationCode1 and locationCode2
		            if (!isValidPlaceCode(locationCode1)) {
		                errors.rejectValue("locations[" + i + "].locationCode1", "invalidFormat.bookLocation.locationCode1", codes, "Must be number between {0} and {1}");
		            }
		            if (!isValidPlaceCode(locationCode2)) {
		                errors.rejectValue("locations[" + i + "].locationCode2", "invalidFormat.bookLocation.locationCode2", codes, "Must be number between {0} and {1}");
		            }
		
		            // Validate the difference between locationCode1 and locationCode2
		            if (!isValidDifference(locationCode1, locationCode2)) {
		                errors.rejectValue("locations[" + i + "].locationCode1", "invalidDifference.bookLocation.placeCodes", codes, "Difference must be {2}");
		            }
		
		            // Validate locationName (only letters allowed)
		            if (!isValidPlaceName(locationName)) {
		                errors.rejectValue("locations[" + i + "].locationName", "invalidFormat.bookLocation.locationName", "Location name can only contain letters.");
		            }
		
		            // Check if the location already exists for the book
		            if (locationRepository.existsByLocationCode1AndLocationCode2AndLocationName(locationCode1, locationCode2, locationName)) {
		                errors.reject("duplicateLocation.bookLocation", "This location already exists for the book.");
		            }
		          }
            }
            
            
        }
    }

    private boolean isValidPlaceCode(String code) {
        try {
            int placeCode = Integer.parseInt(code);
            return placeCode >= 50 && placeCode <= 300;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidDifference(String code1, String code2) {
        try {
            int placeCode1 = Integer.parseInt(code1);
            int placeCode2 = Integer.parseInt(code2);
            return Math.abs(placeCode1 - placeCode2) >= 50;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidPlaceName(String name) {
        return name.matches("[a-zA-Z]+");
    }

	private boolean isValidURL(String url) {
	    try {
	        new URL(url);
	        return true;
	    } catch (MalformedURLException e) {
	        return false;
	    }
	}


	@Override
	public boolean supports(Class<?> clazz) {
		return Book.class.isAssignableFrom(clazz);
	}
}
