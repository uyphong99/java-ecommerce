package com.shopme.admin.setting.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;

@SpringBootTest
@AutoConfigureMockMvc
public class StateRestControllerTests {
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private StateRepository repository;
	
	@Test
	@WithMockUser(username = "nam@codejava.net", password = "something", authorities = "Admin")
	void testListStateByCountry() throws Exception {
		String savingURL = "/states/list_by_country/242";
		
		MvcResult result = mockMvc.perform(get(savingURL))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		StateDTO[] stateDTOs = objectMapper.readValue(jsonResponse, StateDTO[].class);
		
		assertThat(stateDTOs.length).isGreaterThan(55);
	}
	
	@Test
	@WithMockUser(username = "nam@codejava.net", password = "something", authorities = "Admin")
	void testSaveState() throws JsonProcessingException, Exception {
		Country vietnam = new Country(242);
		State state = new State("Kon Tum",vietnam);
		
		String saveURL = "/states/save";
		MvcResult result = mockMvc.perform(post(saveURL)
				.contentType("application/json")
				.content(objectMapper.writeValueAsString(state))
						.with(csrf()))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();
		
		String jsonResponse = result.getResponse().getContentAsString();
		Integer stateId = Integer.valueOf(jsonResponse);
		
		State savedState = repository.findById(stateId).get();
		
		assertThat(savedState.getCountry().getName().equals("Vietnam"));
		assertThat(savedState.getName().equals("Kon Tum"));
	}
}
