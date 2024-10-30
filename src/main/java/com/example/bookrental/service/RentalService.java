package com.example.bookrental.service;

import com.example.bookrental.model.Book;
import com.example.bookrental.model.Rental;
import com.example.bookrental.model.User;
import com.example.bookrental.repository.BookRepository;
import com.example.bookrental.repository.RentalRepository;
import com.example.bookrental.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public Rental rentBook(Long bookId, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        // Check if the user has already rented two books
        long activeRentals = rentalRepository.countByUserAndReturnDateIsNull(user);
        if (activeRentals >= 2) {
            throw new RuntimeException("Cannot rent more than two books");
        }

        // Check if the book is available
        if (!book.getAvailable()) {
            throw new RuntimeException("Book is not available");
        }

        // Rent the book
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setBook(book);
        rental.setRentalDate(new Date());
        book.setAvailable(false);

        return rentalRepository.save(rental);
    }

    public Rental returnBook(Long bookId, String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        Rental rental = rentalRepository.findByBookAndUserAndReturnDateIsNull(bookId, user.getId())
            .orElseThrow(() -> new RuntimeException("Rental record not found"));

        rental.setReturnDate(new Date());
        rental.getBook().setAvailable(true);

        return rentalRepository.save(rental);
    }

    public List<Rental> getUserRentals(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        return rentalRepository.findByUser(user);
    }
}
