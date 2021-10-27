package com.backendapi.hotelmanagement.repository;

import com.backendapi.hotelmanagement.domain.Role;
import com.backendapi.hotelmanagement.domain.enumeration.UserRole;
import com.backendapi.hotelmanagement.exception.ResourceNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(UserRole name);
}
