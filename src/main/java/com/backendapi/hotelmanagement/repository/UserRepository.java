package com.backendapi.hotelmanagement.repository;

import com.backendapi.hotelmanagement.domain.User;
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

    Optional<User> findByUsername(String userName);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.email = ?2, u.fullName = ?3, u.phoneNumber = ?4, u.ssn = ?5, u.drivingLicense = ?6, " +
            "u.country = ?7, u.state = ?8, u.address = ?9, u.workingSector = ?10, u.birthDate = ?11 WHERE u.id =?1")
    void update(Long id, String email, String fullName, String phoneNumber, String ssn, String drivingLicense,
                String country, String state, String address, String workingSector, Date birthDate);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.password = ?2 WHERE u.id =?1")
    void updatePassword(Long id, String password);

    @Transactional
    @Modifying
    @Query("UPDATE User u " +
            "SET u.enabled = TRUE WHERE u.email =?1")
    int enableAppUser(String email);
}
