package com.example.episodicshows.viewings;

import com.example.episodicshows.shows.Episode;
import com.example.episodicshows.shows.EpisodeRepository;
import com.example.episodicshows.shows.Show;
import com.example.episodicshows.shows.ShowRepository;
import com.example.episodicshows.users.User;
import com.example.episodicshows.users.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ViewingsServiceTest {

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

	private Long userId;
	private Long cosmosId;
	private Long lostWorldsId;
	private Viewing existingViewing;
	private LocalDateTime existingViewingTime;
	private Viewing requestBody;
	private LocalDateTime requestBodyTime;

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

		requestBodyTime = LocalDateTime.now();
		requestBody = Viewing.builder()
				.episodeId(lostWorldsId)
				.updatedAt(requestBodyTime)
				.timeCode(43)
				.build();
	}

	@Transactional
	@Rollback
	@Test
	public void getRecentlyViewed_returnsRecentlyViewed() throws Exception {
		viewingRepository.save(existingViewing);

		assertThat(viewingsService.getRecentlyWatched(userId).size(), equalTo(1));
	}

	@Transactional
	@Rollback
	@Test
	public void createOrUpdateViewing_createsNewViewing() throws Exception {
		Long count = viewingRepository.count();

		Viewing result = viewingsService.createOrUpdateViewing(userId, requestBody);

		assertThat(result.getEpisodeId(), equalTo(lostWorldsId));
		assertThat(result.getUpdatedAt(), equalTo(requestBodyTime));
		assertThat(result.getTimeCode(), equalTo(43));
		assertThat(viewingRepository.count(), equalTo(count + 1));
	}

	@Transactional
	@Rollback
	@Test
	public void createOrUpdateViewing_updatesExistingViewing() throws Exception {
		Viewing existing = viewingRepository.save(existingViewing);
		Long count = viewingRepository.count();

		Viewing result = viewingsService.createOrUpdateViewing(userId, requestBody);
		assertThat(result.getId(), equalTo(existing.getId()));
		assertThat(result.getEpisodeId(), equalTo(existing.getEpisodeId()));
		assertThat(result.getTimeCode(), equalTo(requestBody.getTimeCode()));
		assertThat(result.getUpdatedAt(), equalTo(requestBody.getUpdatedAt()));
//		assertThat(result.getUpdatedAt(), equalTo(requestBody.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS)));
		assertThat(viewingRepository.count(), equalTo(count));
	}
}