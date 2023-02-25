package com.shopme.admin.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.exception.UserNotFoundException;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import ch.qos.logback.core.joran.conditional.IfAction;

@Service
public class UserService {
	
	public static final int USERS_PER_PAGE = 5;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public List<User> listAll() {
		return (List<User>) userRepo.findAll();
	}
	
	public Page<User> listByPage(int pageNumber, String sortField, String sortDir, String keyword) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNumber - 1, USERS_PER_PAGE, sort);
		Page<User> page = (keyword == null) ? userRepo.findAll(pageable) : userRepo.findAll(keyword, pageable);
		return page;
	}
	
	public List<Role> listRoles() {
		return (List<Role>) roleRepo.findAll();
	}

	public User save(User user) {
		
		Boolean isUpdatingUser = user.getId() != null;
		
		if (isUpdatingUser) {
			User existingUser = userRepo.findById(user.getId()).get();
			
			if (user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			} else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		
		return userRepo.save(user);
		
	}
	
	public void encodePassword(User user) {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
	}
	
	/*
	 * This method will handle the post request of form's onSubmit
	 * */
	public boolean isDuplicatedEmail(Integer id ,String email) {
		User user = userRepo.findUserByEmail(email);
		
		if (userRepo.existsByEmail(email)) {
			if (id != null) {
				if (id != user.getId()) return true;
			} else if (id == null) {
				return true;
			}
		}
		
		return false;		
	}
	
	public User getUserById(Integer id) {
		if(!userRepo.existsById(id)) {
			throw new UserNotFoundException("Couldn't find any user with provided ID " + id);
		}
		return userRepo.findById(id).get();
	}
	
	public Boolean isExistedEmail(String email) {
		return userRepo.existsByEmail(email);
	}
	
	public void deleteUser(Integer id) {
		userRepo.deleteById(id);
	}
	
	public void enableUserById(Integer id) {
		User user = userRepo.findById(id).get();
		
		user.setEnabled(!user.isEnabled());
		
		userRepo.save(user);
	}
	
	public User updateAccount(User user) {
		User userExisted = userRepo.findById(user.getId()).get();
		
		if (!user.getPassword().isEmpty()) {
			userExisted.setPassword(passwordEncoder.encode(user.getPassword()));
		}
		
		if (user.getPhotos() != null) {
			userExisted.setPhotos(user.getPhotos());
		}
		
		
		userExisted.setFirstName(user.getFirstName());
		userExisted.setLastName(user.getLastName());
		
		return userRepo.save(userExisted);
	}
}
