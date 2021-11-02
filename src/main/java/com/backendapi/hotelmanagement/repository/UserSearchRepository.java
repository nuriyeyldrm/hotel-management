package com.backendapi.hotelmanagement.repository;

import com.backendapi.hotelmanagement.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface UserSearchRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
}
