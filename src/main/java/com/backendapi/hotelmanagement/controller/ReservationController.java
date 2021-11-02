package com.backendapi.hotelmanagement.controller;

import com.backendapi.hotelmanagement.domain.Reservation;
import com.backendapi.hotelmanagement.service.ReservationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@RestController
@Produces(MediaType.APPLICATION_JSON)
@RequestMapping("/api/reservation")
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.fetchAllReservations();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationByUsername(@PathVariable Long id) {
        Reservation reservation = reservationService.findById(id);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @GetMapping("/all{id}")
    public ResponseEntity<List<Reservation>> getAllReservationById(@PathVariable Long id) {
        List<Reservation> reservation = reservationService.findAllById(id);
        return new ResponseEntity<>(reservation, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Map<String, Boolean>> createReservation(@Valid @RequestBody Reservation reservation) {
        reservationService.createReservation(reservation);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> updateReservation(@PathVariable Long id,
                                                           @Valid @RequestBody Reservation reservation) {
        reservation.setId(id);
        reservationService.updateReservation(reservation);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        Map<String, Boolean> map = new HashMap<>();
        map.put("success", true);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
}
