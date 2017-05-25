package com.example.episodicshows.viewings;

import com.example.episodicshows.TestBase;
import com.example.episodicshows.shows.Episode;
import com.example.episodicshows.shows.EpisodeRepository;
import com.example.episodicshows.shows.Show;
import com.example.episodicshows.shows.ShowRepository;
import com.example.episodicshows.users.User;
import com.example.episodicshows.users.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ViewingsControllerTest extends TestBase {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ShowRepository showRepository;

	@Autowired
	private EpisodeRepository episodeRepository;

	@Autowired
	private ViewingRepository viewingRepository;

	@Autowired
	private ViewingsService viewingsService;

	@Autowired
	private ObjectMapper objectMapper;

	private Long userId;
	private Long cosmosId;
	private Long lostWorldsId;
	private Viewing existingViewing;
	private LocalDateTime existingViewingTime;
	private Viewing newViewing;
	private LocalDateTime newViewingTime;

	@Before
	public void  seedData() {
		User user = User.builder()
				.email("someone@email.com")
				.build();
		userId = userRepository.save(user).getId();

		Show cosmos = Show.builder()
				.name("Cosmos: A Spacetime Odyssey")
				.build();
		cosmosId = showRepository.save(cosmos).getId();

		Episode lostWorlds = Episode.builder()
				.seasonNumber(2)
				.episodeNumber(9)
				.showId(cosmosId)
				.build();
		lostWorldsId = episodeRepository.save(lostWorlds).getId();

		existingViewingTime = LocalDateTime.now();
		existingViewing = Viewing.builder()
				.userId(userId)
				.showId(cosmosId)
				.episodeId(lostWorldsId)
				.updatedAt(existingViewingTime)
				.timeCode(37)
				.build();

		newViewingTime = LocalDateTime.now();
		newViewing = Viewing.builder()
				.episodeId(lostWorldsId)
				.updatedAt(newViewingTime)
				.timeCode(43)
				.build();
	}

	@Transactional
	@Rollback
	@Test
	public void getRecentViewingsTest () throws Exception {
		viewingRepository.save(existingViewing);

		MockHttpServletRequestBuilder request = get("/users/{userId}/recently-watched", userId)
				.accept(MediaType.APPLICATION_JSON);

		this.mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.[0].show.id", equalTo(cosmosId.intValue())))
				.andExpect(jsonPath("$.[0].episode.id", equalTo(lostWorldsId.intValue())))
				.andExpect(jsonPath("$.[0].updatedAt", equalTo(existingViewingTime.toString())))
				.andExpect(jsonPath("$.[0].timeCode", equalTo(37)));
	}

	@Transactional
	@Rollback
	@Test
	public void patchCreateNewViewingTest () throws Exception {
		Long count = viewingRepository.count();

		String json = objectMapper.writeValueAsString(newViewing);

		MockHttpServletRequestBuilder request = patch("/users/{userId}/viewings", userId)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		this.mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.episodeId", equalTo(lostWorldsId.intValue())))
				.andExpect(jsonPath("$.updatedAt", equalTo(newViewingTime.truncatedTo(ChronoUnit.MILLIS).toString())))
				.andExpect(jsonPath("$.timeCode", equalTo(43)));

		assertThat(viewingRepository.count(), equalTo(count + 1));
	}

	@Transactional
	@Rollback
	@Test
	public void patchUpdateExistingViewingTest () throws Exception {
		viewingRepository.save(existingViewing);

		Long count = viewingRepository.count();

		String json = objectMapper.writeValueAsString(newViewing);

		MockHttpServletRequestBuilder request = patch("/users/{userId}/viewings", userId)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json);

		this.mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.episodeId", equalTo(lostWorldsId.intValue())))
				.andExpect(jsonPath("$.updatedAt", equalTo(newViewingTime.toString())))
				.andExpect(jsonPath("$.timeCode", equalTo(43)));

		assertThat(viewingRepository.count(), equalTo(count));
	}

}