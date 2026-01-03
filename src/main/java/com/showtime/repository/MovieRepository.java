package com.showtime.repository;

import com.showtime.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByIsActiveTrueOrderByReleaseDateDesc();
    
    @Query("SELECT m FROM Movie m WHERE m.isActive = true AND " +
           "(LOWER(m.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.cast) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.director) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(m.genre) LIKE LOWER(CONCAT('%', :query, '%')))")
    List<Movie> searchMovies(@Param("query") String query);
    
    @Query("SELECT DISTINCT m FROM Movie m JOIN m.shows s WHERE " +
           "s.showDate >= :date AND s.isActive = true AND m.isActive = true " +
           "AND (:city IS NULL OR LOWER(s.theater.city) LIKE LOWER(CONCAT('%', :city, '%'))) " +
           "AND (:language IS NULL OR LOWER(m.language) LIKE LOWER(CONCAT('%', :language, '%'))) " +
           "AND (:genre IS NULL OR LOWER(m.genre) LIKE LOWER(CONCAT('%', :genre, '%')))")
    List<Movie> findMoviesWithFilters(
        @Param("date") LocalDate date,
        @Param("city") String city,
        @Param("language") String language,
        @Param("genre") String genre
    );
    
    @Query("SELECT m FROM Movie m WHERE m.releaseDate > CURRENT_DATE ORDER BY m.releaseDate")
    List<Movie> findUpcomingMovies();
    
    List<Movie> findByGenreContainingIgnoreCase(String genre);
    List<Movie> findByLanguageContainingIgnoreCase(String language);
    
    @Query("SELECT COUNT(m) FROM Movie m WHERE m.isActive = true")
    Long countActiveMovies();
}