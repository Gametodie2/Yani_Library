package repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import domain.BookLocation;

@Repository
public interface BookLocationRepository extends CrudRepository<BookLocation, String>{
	
	@Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM BookLocation l WHERE l.locationCode1 = :locationCode1 AND l.locationCode2 = :locationCode2 AND l.locationName = :locationName")
    boolean existsByLocationCode1AndLocationCode2AndLocationName(@Param("locationCode1") String locationCode1, @Param("locationCode2") String locationCode2, @Param("locationName") String locationName);
}