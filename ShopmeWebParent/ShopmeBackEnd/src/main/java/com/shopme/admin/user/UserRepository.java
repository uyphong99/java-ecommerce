package com.shopme.admin.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>{
	
	//@Query("SELECT u FROM User u WHERE u.email= :email")
	public User findUserByEmail(/*@Param("email")*/ String email);
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName,' ', u.lastName) LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);
		
	
	public boolean existsByEmail(String email);
}
