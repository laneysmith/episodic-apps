package com.example.episodicshows.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	UserRepository userRepository;

	@Test
	@Transactional
	@Rollback
	public void postUser_createsNewUser() throws Exception {
		Long count = userRepository.count();

		Map<String, Object> payload = new HashMap<String, Object>() {
			{
				put("email", "squidward@tentacles.com");
			}
		};

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(payload);

		MockHttpServletRequestBuilder request = post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.email", equalTo("squidward@tentacles.com")));

		assertThat(userRepository.count(), equalTo(count + 1));
	}

	@Test
	@Transactional
	@Rollback
	public void getUser_returnsAllUsers() throws Exception {
		User user = new User();
		user.setEmail("spongebob@squarepants.com");
		userRepository.save(user);

		MockHttpServletRequestBuilder request = get("/users")
				.contentType(MediaType.APPLICATION_JSON);

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", equalTo(user.getId().intValue())))
				.andExpect(jsonPath("$[0].email", equalTo("spongebob@squarepants.com")));

		assertThat(userRepository.count(), equalTo(1L));
	}

}
