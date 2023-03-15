package com.shopme.admin.setting;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;


@SpringBootTest
@AutoConfigureMockMvc
public class RestControllerTests {
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private CountryRepository repository;
	
	
	
	@Test
	@WithMockUser(username = "nam@codejava.net", password = "something", authorities = "Admin")
	void countryRestControllerTest() throws Exception {
		MvcResult result = mockMvc.perform(get("/countries/list"))
			.andExpect(status().isOk())
			.andDo(print())
			.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		Country[] countries = objectMapper.readValue(jsonResponse, Country[].class);
		
		assertThat(countries).hasSizeGreaterThan(0);
	}
	
	@Test
	@WithMockUser(username = "nam@codejava.net", password = "something", authorities = "Admin")
	void countrySaveTest() throws Exception {
		Country country = new Country("Germany", "DE"); 
		
		MvcResult result = mockMvc.perform(post("/countries/save").contentType("application/json")
				.content(objectMapper.writeValueAsString(country))
				.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		Integer countryId = Integer.parseInt(jsonResponse);
		
		Optional<Country> savedCountry = repository.findById(countryId);
		
		assertThat(savedCountry.isPresent());
		assertThat(savedCountry.get().getName()).isEqualTo("Germany");
		
	}
	
	
}
