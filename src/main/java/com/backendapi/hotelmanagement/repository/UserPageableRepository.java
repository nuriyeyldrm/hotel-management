package com.backendapi.hotelmanagement.repository;

import com.backendapi.hotelmanagement.domain.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPageableRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {
}
