package repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import domain.Book;

public interface BookRepository extends CrudRepository<Book, String> {

	List<Book> findByTitle(String title);
	
	@Query("SELECT b FROM Book b WHERE size(b.favoritedByUsers) > 0 ORDER BY size(b.favoritedByUsers) DESC, b.title ASC")
    List<Book> findMostLikedBooks();
	
	@Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END FROM Book b WHERE b.isbn = :isbn")
    boolean existsByIsbn(@Param("isbn") String isbn);

	@Query("SELECT b FROM Book b JOIN b.authors a WHERE a.name = :author")
    List<Book> findBooksByAuthor(String author);

	@Query("SELECT b FROM Book b WHERE b.isbn = :isbn")
	Book findBookByIsbn(@Param("isbn") String isbn);

}
