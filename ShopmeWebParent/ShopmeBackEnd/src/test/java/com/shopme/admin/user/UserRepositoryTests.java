package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.booleanThat;
import static org.mockito.ArgumentMatchers.intThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTests {
	@Autowired
	private UserRepository repo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateNewUserWithOneRole() {
		Role adminRole = entityManager.find(Role.class, 1);
		User userNamHM = new User("nam@codejava.net", "nam2020", "Nam", "Ha Minh");
		userNamHM.addRole(adminRole);
		
		User savedUser = repo.save(userNamHM);
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testCreateNewUserWithTwoRole() {
		User userRavil = new User("ravil@gmail.com", "ravi2000", "Ravi", "Kumar");
		userRavil.addRole(new Role(4));
		userRavil.addRole(new Role(6));
		
		User savedUser = repo.save(userRavil);
		
		assertThat(savedUser.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testListAllUsers() {
		Iterable<User> listUsers = repo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
	@Test
	public void testGetUserById() {
		User userNam = repo.findById(1).get();
		System.out.println(userNam);
		assertThat(userNam).isNotNull();
	}
	
	@Test
	public void testUpdateUserDetails() {
		User userNam = repo.findById(1).get();
		userNam.setEnabled(true);
		userNam.setEmail("namjavaprogrammer@codejava.com");
		
		repo.save(userNam);
	}
	
	@Test
	public void testUpdateUserRoles() {
		User raviUser = repo.findById(2).get();
		Role editorRole = new Role(4);
		Role saleRole = new Role(3);
		
		raviUser.getRoles().remove(editorRole);
		raviUser.addRole(saleRole);
		
		repo.save(raviUser);
	}
	
	@Test
	public void testDeleteUser() {
		repo.deleteById(2);
	}
	
	@Test
	public void testGetUserByEmail() {
		String emailString = "uyphong99@gmail.com";
		User user = repo.findUserByEmail(emailString);
		
		assertThat(user).isNotNull();
	}
	
	@Test
	public void testExistEmail() {
		boolean existEmail =  repo.existsByEmail("uyphong99@gmail.com");
		
		assertThat(existEmail).isTrue();
	}
	
	@Test
	public void listFirstPageTest() {
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(pageable);
		
		List<User> list = page.getContent();
		
		assertThat(list).hasSize(pageSize);
	}
	
	@Test
	public void testSearchResult() {
		String keyword = "bruce";
		
		int pageNumber = 0;
		int pageSize = 4;
		
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<User> page = repo.findAll(keyword, pageable);
		
		List<User> list = page.getContent();
		
		assertThat(list.size()).isGreaterThan(0);
	}
}
