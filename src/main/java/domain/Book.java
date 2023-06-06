package domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "book")
public class Book {
	@JsonProperty("book_id")
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
	
    @NotBlank
    private String title;
	
	@JsonProperty("cover")
    private String image;
   
    private String isbn;
    
    private double price;
    
    private double rating;
    
    @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<BookLocation> locations = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
        )
    @JsonManagedReference
    private List<Author> authors = new ArrayList<>();
    
    @ManyToMany(mappedBy = "favoriteBooks")
    @JsonManagedReference
    private List<User> favoritedByUsers;

    public Book() {

    }
    
    public Book(String title, String image, String isbn, double price) {
    	this.title=title;
    	this.image=image;
    	this.isbn = isbn;
    	this.price = price;
    }
    public Book(String title, String image, String isbn, double price, List<Author> authors) {
    	this.title=title;
    	this.image=image;
    	this.isbn = isbn;
    	this.price = price;
    	this.authors = authors;
    }

    public String getId() {
        return id;
    }
    
    public int getLikes() {
    	return favoritedByUsers != null ? favoritedByUsers.size() : 0;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
    	this.title = title;
    }

    public String getImage() {
        return image;
    }
    
    public void setImage(String image) {
    	this.image = image;
    }
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
    	this.isbn = isbn;
    }

    public double getPrice() {
        return price;
    }
    
    public String priceToString() {
    	return String.format("%.2f", price);
    }
    
    public void setPrice(double price) {
    		this.price = price;
    }

    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }

    public List<BookLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<BookLocation> locations) {
        this.locations = locations;
    }

    public void addLocation(BookLocation location) {
        locations.add(location);
    }

    public void removeLocation(BookLocation location) {
        locations.remove(location);
    }
    
    public String locationsToString() {
    	return locations.stream().map(a -> String.format("%s %s %s", a.getLocationCode1(), a.getLocationCode2(), a.getLocationName())).collect(Collectors.joining(", "));
    }
    
    public List<Author> getAuthors(){
    	return authors;
    }
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
    
    public String authorsToString() {
    	return authors.stream().map(a -> String.format("%s", a.getName())).collect(Collectors.joining(", "));
    }
    
    public void addAuthors(Author author) {
    	authors.add(author);
    }
    public void removeAuthor(Author author) {
    	authors.remove(author);
    }
    
    public int compareTo(Book other) {
        if (this.getLikes() != other.getLikes()) {
            return Integer.compare(other.getLikes(), this.getLikes());
        } else {
            return this.title.compareToIgnoreCase(other.title);
        }
    }
}




