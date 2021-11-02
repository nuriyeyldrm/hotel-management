package com.backendapi.hotelmanagement.repository;

import com.backendapi.hotelmanagement.domain.User;
import com.backendapi.hotelmanagement.exception.BadRequestException;
import com.backendapi.hotelmanagement.exception.ConflictException;
import com.backendapi.hotelmanagement.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username) throws ResourceNotFoundException;

    Boolean existsByUsername(String username) throws ConflictException;

    Boolean existsByEmail(String email) throws ConflictException;

    Boolean existsBySsn(String ssn) throws  ConflictException;

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.email = ?2, u.fullName = ?3, u.phoneNumber = ?4, u.ssn = ?5, u.drivingLicense = ?6, " +
            "u.country = ?7, u.state = ?8, u.address = ?9, u.workingSector = ?10, u.birthDate = ?11 " +
            "WHERE u.username = ?1")
    void update(String username, String email, String fullName, String phoneNumber, String ssn, String drivingLicense,
                String country, String state, String address, String workingSector, Date birthDate)
            throws BadRequestException;

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.enabled = TRUE WHERE u.email =?1")
    int enableAppUser(String email);
}
