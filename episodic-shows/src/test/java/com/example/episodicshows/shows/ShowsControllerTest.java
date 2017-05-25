package com.example.episodicshows.shows;

import com.example.episodicshows.TestBase;
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
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ShowsControllerTest extends TestBase {

	@Autowired
	MockMvc mvc;

	@Autowired
	ShowRepository showRepository;

	@Test
	@Transactional
	@Rollback
	public void postShow_createsNewShow() throws Exception {
		Long count = showRepository.count();

		Map<String, Object> payload = new HashMap<String, Object>() {
			{
				put("name", "Some show");
			}
		};

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(payload);

		MockHttpServletRequestBuilder request = post("/shows")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.name", equalTo("Some show")));

		assertThat(showRepository.count(), equalTo(count + 1));
	}

	@Test
	@Transactional
	@Rollback
	public void getShows_returnsAllShows() throws Exception {
		Show show = Show.builder()
				.name("Some show")
				.build();
		showRepository.save(show);

		MockHttpServletRequestBuilder request = get("/shows")
				.contentType(MediaType.APPLICATION_JSON);

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id", equalTo(show.getId().intValue())))
				.andExpect(jsonPath("$[0].name", equalTo("Some show")));

		assertThat(showRepository.count(), equalTo(1L));
	}

}