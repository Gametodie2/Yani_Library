package domain;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "book_location")
public class BookLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    private String locationCode1;
    
    private String locationCode2;
    
    private String locationName;
    
    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonBackReference
    private Book book;

    public BookLocation() {
    	
    }
    
    public BookLocation(String locationCode1, String locationCode2, String locationName) {
    	this.locationCode1 = locationCode1;
    	this.locationCode2 = locationCode2;
    	this.locationName = locationName;
    }
    
    public String getId() {
        return id;
    }

    public String getLocationCode1() {
        return locationCode1;
    }

    public void setLocationCode1(String locationCode1) {
        this.locationCode1 = locationCode1;
    }

    public String getLocationCode2() {
        return locationCode2;
    }

    public void setLocationCode2(String locationCode2) {
        this.locationCode2 = locationCode2;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
    
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}

