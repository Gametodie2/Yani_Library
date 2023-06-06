package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.User;

public interface UserRepository extends JpaRepository<User, String>{
	User findByUsername(String username);
}
