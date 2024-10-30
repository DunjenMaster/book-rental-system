package com.example.bookrental.controller;

import com.example.bookrental.model.Rental;
import com.example.bookrental.service.RentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/books/{bookId}/rent")
    public ResponseEntity<Rental> rentBook(@PathVariable Long bookId, @RequestBody Map<String, String> payload) {
        String userEmail = payload.get("userEmail");
        Rental rental = rentalService.rentBook(bookId, userEmail);
        return new ResponseEntity<>(rental, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/books/{bookId}/return")
    public ResponseEntity<Rental> returnBook(@PathVariable Long bookId, @RequestBody Map<String, String> payload) {
        String userEmail = payload.get("userEmail");
        Rental rental = rentalService.returnBook(bookId, userEmail);
        return new ResponseEntity<>(rental, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("/users/{userEmail}")
    public ResponseEntity<List<Rental>> getUserRentals(@PathVariable String userEmail) {
        List<Rental> rentals = rentalService.getUserRentals(userEmail);
        return new ResponseEntity<>(rentals, HttpStatus.OK);
    }
}
