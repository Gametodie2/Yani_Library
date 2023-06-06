package repository;


import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {
}

