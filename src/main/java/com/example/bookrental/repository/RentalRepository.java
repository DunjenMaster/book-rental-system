package com.example.bookrental.repository;

import com.example.bookrental.model.Rental;
import com.example.bookrental.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    @Query("SELECT COUNT(r) FROM Rental r WHERE r.user = :user AND r.returnDate IS NULL")
    long countByUserAndReturnDateIsNull(@Param("user") User user);

    @Query("SELECT r FROM Rental r WHERE r.book.id = :bookId AND r.user.id = :userId AND r.returnDate IS NULL")
    Optional<Rental> findByBookAndUserAndReturnDateIsNull(@Param("bookId") Long bookId, @Param("userId") Long userId);

    List<Rental> findByUser(User user);
}
