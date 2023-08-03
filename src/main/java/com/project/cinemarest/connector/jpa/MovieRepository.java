package com.project.cinemarest.connector.jpa;

import com.project.cinemarest.entity.Movie;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepository extends MongoRepository<Movie, String> {

    List<Movie> findAll();

    @Query("{'title': :#{#title}}")
    Optional<Movie> findMovieByTitle(@Param("title") String title);
}
