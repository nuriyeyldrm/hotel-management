package com.backendapi.hotelmanagement.repository;

import com.backendapi.hotelmanagement.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllById(Long id);

}
