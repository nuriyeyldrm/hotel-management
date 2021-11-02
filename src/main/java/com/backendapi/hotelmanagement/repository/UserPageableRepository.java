package com.backendapi.hotelmanagement.repository;

import com.backendapi.hotelmanagement.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface UserPageableRepository extends PagingAndSortingRepository<User, Long> {

    @Query("select u from User u " +
            "where (u.id = :id or (:id is null))  and (u.username = :username or (:username is null)) and " +
            "(u.email = :email or (:email is null)) and (u.fullName = :fullName or (:fullName is null)) and " +
            "(u.birthDate = :birthDate) and " +
            "(u.phoneNumber = :phoneNumber or (:phoneNumber is null)) and (u.enabled = :enabled)"
    )
    Page<User> findAllByLocation(@Param("id") Long id,
                                 @Param("username") String username,
                                 @Param("email") String email,
                                 @Param("fullName") String fullName,
                                 @Param("birthDate") Date birthDate,
                                 @Param("phoneNumber") String phoneNumber,
                                 @Param("enabled") Boolean enabled,
                                 Pageable pageable);
}
