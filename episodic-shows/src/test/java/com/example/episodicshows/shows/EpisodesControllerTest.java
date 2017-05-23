package com.example.episodicshows.shows;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EpisodesControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	EpisodeRepository episodeRepository;

	@Autowired
	ShowRepository showRepository;

	@Test
	@Transactional
	@Rollback
	public void postEpisode_createsNewEpisode() throws Exception {
		Show show = showRepository.save(Show.builder()
				.name("Some show")
				.build());
		Long count = episodeRepository.count();

		Map<String, Integer> payload = new HashMap<String, Integer>() {
			{
				put("seasonNumber", 4);
				put("episodeNumber", 7);
			}
		};

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(payload);

		MockHttpServletRequestBuilder request = post("/shows/{showId}/episodes", show.getId())
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.showId", equalTo(show.getId().intValue())))
				.andExpect(jsonPath("$.seasonNumber", equalTo(4)))
				.andExpect(jsonPath("$.episodeNumber", equalTo(7)))
				.andExpect(jsonPath("$.title", equalTo("S4 E7")));

		assertThat(episodeRepository.count(), equalTo(count + 1));
	}

	@Test
	@Transactional
	@Rollback
	public void getAllEpisodesByShowId_returnsListOfEpisodes() throws Exception {
		Show firstShowResult = showRepository.save(Show.builder()
				.name("First show")
				.build());
		Show secondShowResult = showRepository.save(Show.builder()
				.name("Second show")
				.build());
		Long showId = firstShowResult.getId();

		Episode firstShows1e1 = Episode.builder()
				.showId(showId)
				.seasonNumber(1)
				.episodeNumber(1)
				.build();
		Episode firstShows1e2 = Episode.builder()
				.showId(showId)
				.seasonNumber(1)
				.episodeNumber(2)
				.build();
		Episode secondShows1e1 = Episode.builder()
				.showId(secondShowResult.getId())
				.seasonNumber(4)
				.episodeNumber(5)
				.build();
		episodeRepository.save(firstShows1e1);
		episodeRepository.save(firstShows1e2);
		episodeRepository.save(secondShows1e1);

		MockHttpServletRequestBuilder request = get("/shows/{showId}/episodes", showId)
				.contentType(MediaType.APPLICATION_JSON);

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", equalTo(2)))
				.andExpect(jsonPath("$[0].episodeNumber", equalTo(1)))
				.andExpect(jsonPath("$[1].episodeNumber", equalTo(2)));
	}
}