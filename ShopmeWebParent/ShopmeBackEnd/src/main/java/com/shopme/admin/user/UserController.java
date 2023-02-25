package com.shopme.admin.user;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.user.exception.UserNotFoundException;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;


import  org.springframework.util.StringUtils;



@Controller
public class UserController {
	
	@Autowired
	private UserService service;
	
	@GetMapping("/users")
	public String listAll(Model model) {
		return listByPage(1, model, "firstName", "asc", null);
	}
	
	@GetMapping("/users/page/{pageNumber}")
	public String listByPage(@PathVariable("pageNumber") int pageNumber, Model model,
				@Param("sortField") String sortField, @Param("sortDir") String sortDir,
				@Param("keyword") String keyword) {
		Page<User> pageUser = service.listByPage(pageNumber, sortField, sortDir, keyword);
		List<User> listUsers = pageUser.getContent();
		
		long startCount = (pageNumber - 1) * UserService.USERS_PER_PAGE + 1;
		long endCount = startCount + UserService.USERS_PER_PAGE - 1;
		
		if (endCount > pageUser.getTotalElements()) {
			endCount = pageUser.getTotalElements();
		} 
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("totalPages", pageUser.getTotalPages());
		model.addAttribute("currentPage", pageNumber);
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageUser.getTotalElements());
		model.addAttribute("listUsers", listUsers);
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		
		return "users/users";
	}
	
	@GetMapping("/users/new")
	public String newUser(Model model) {
		List<Role> roles = service.listRoles();
		User user = new User();
		user.setEnabled(true);
		
		model.addAttribute("user", user);
		model.addAttribute("roles",roles);
		model.addAttribute("pageTitle", "Create new user");
		
		return "users/user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user, RedirectAttributes redirectAttributes,
			@RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User savedUser = service.save(user);
			
			String uploadDir = "user-photos/" + savedUser.getId();
			FileUploadUtil.cleanDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			if (user.getPhotos().isEmpty()) user.setPhotos(null);
			service.save(user);
		}
		
		redirectAttributes.addFlashAttribute("message", "The user has been saved successfully.");
		return getUrlToEditedUser(user);
	}

	private String getUrlToEditedUser(User user) {
		String emailEditedUser = user.getEmail();
		
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword=" + emailEditedUser;
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable Integer id, 
			Model model,
			RedirectAttributes redirectAttributes) {
		try {
			List<Role> roles = service.listRoles();
			User user = service.getUserById(id);
			
			model.addAttribute("user", user);
			model.addAttribute("roles",roles);
			model.addAttribute("pageTitle", "Edit user (ID: " + id + ")");
			
			return "users/user_form";
		} catch (UserNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable("id") Integer id, 
			RedirectAttributes redirectAttributes) {
		
		service.deleteUser(id);
		
		redirectAttributes.addFlashAttribute("message", "The user has been deleted.");
		return "redirect:/users";
	}
	
	@GetMapping("/users/enable/{id}")
	public String enable(@PathVariable("id") Integer id,
			RedirectAttributes redirectAttributes) {
		String isEnable = service.getUserById(id).isEnabled() ? "disable" : "enable";
		service.enableUserById(id);
		redirectAttributes.addFlashAttribute("message", "The user has been " + isEnable + ".");
		return "redirect:/users";
	}
	
	@GetMapping("/users/categories")
	public String categories() {
		return "Hello";
	}
}
