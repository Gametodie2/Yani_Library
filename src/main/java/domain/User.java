package domain;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
	
	@Column(unique = true)
	private String email;
	
	@Column(unique = true)
	private String username;
	
	private String password;
	
	private String avatar;
	
	private int max_fav;
	
	@Enumerated(EnumType.STRING)
    private Role role;
	
	@ManyToMany
    @JoinTable(name = "user_favorite_books",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "book_id"))
	private List<Book> favoriteBooks;

	
	public User(String username, String password, Role role, String avatar, int max_fav) {
		this.username= username;
		this.password = password;
		this.role = role;
		this.avatar= avatar;
		this.max_fav = max_fav;
		this.favoriteBooks = new ArrayList<>();
	}
	
	public User() {
		
	}
	
	public String getAvatar() {
		return this.avatar;
	}
	
	public Role getRole() {
		return this.role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public String getUsername() {
		return this.username;
	}
	
	public void setPassword(String encodedPassword) {
		this.password = encodedPassword;
	}
	
	public List<Book> getBooks() {
		return this.favoriteBooks;
	}
	
	public void addFavoriteBook(Book book) {
		if (favoriteBooks.size() >= max_fav) {
	        throw new IllegalStateException("Cannot add more favorite books. Maximum limit reached.");
	    }
        favoriteBooks.add(book);
    }
	
	public int getFavoriteBooksSize() {
		return favoriteBooks.size();
	}

    public void removeFavoriteBook(Book book) {
        favoriteBooks.remove(book);
    }
    
    public boolean isBookFavorited(Book book) {
        return favoriteBooks.contains(book);
    }
    
    public int getMax_fav() {
    	return this.max_fav;
    }


}
