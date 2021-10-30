package com.backendapi.hotelmanagement.service;

import com.backendapi.hotelmanagement.domain.Reservation;
import com.backendapi.hotelmanagement.exception.BadRequestException;
import com.backendapi.hotelmanagement.exception.ResourceNotFoundException;
import com.backendapi.hotelmanagement.repository.ReservationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private static final String RESERVATION_NOT_FOUND_MSG = "reservation with id %d not found";

    public List<Reservation> fetchAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation findById(Long id) throws ResourceNotFoundException {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(RESERVATION_NOT_FOUND_MSG, id)));
    }

    public List<Reservation> findAllById(Long id) {
        return reservationRepository.findAllById(id);
    }

    public void createReservation(Reservation reservation) throws BadRequestException {
        try {
            reservationRepository.save(reservation);
        } catch (Exception e) {
            throw new BadRequestException("invalid request");
        }
    }

    public void updateReservation(Reservation reservation) throws BadRequestException {
        try {
            reservationRepository.save(reservation);
        } catch (Exception e) {
            throw new BadRequestException("invalid request");
        }
    }

    public void deleteReservation(Long id) throws ResourceNotFoundException {
        boolean reservationExist = reservationRepository.findById(id).isPresent();

        if (!reservationExist)
            throw new ResourceNotFoundException("reservation does not exist");

        reservationRepository.deleteById(id);
    }
}
