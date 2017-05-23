package com.example.episodicshows.viewings;

import com.example.episodicshows.shows.Episode;
import com.example.episodicshows.shows.EpisodeRepository;
import com.example.episodicshows.shows.Show;
import com.example.episodicshows.shows.ShowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ViewingsService {

	private final ShowRepository showRepository;
	private final EpisodeRepository episodeRepository;
	private final ViewingRepository viewingRepository;

	public ViewingsService(
			ShowRepository showRepository,
			EpisodeRepository episodeRepository,
			ViewingRepository viewingRepository
	) {
		this.showRepository = showRepository;
		this.episodeRepository = episodeRepository;
		this.viewingRepository = viewingRepository;
	}

	@Transactional
	public Viewing createOrUpdateViewing(Long userId, Viewing requestBody) {
		Viewing existingViewing = viewingRepository.findByUserIdAndEpisodeId(
				userId,
				requestBody.getEpisodeId());

		if (existingViewing != null) {
			Viewing updatedViewing = new Viewing(
					existingViewing.getId(),
					existingViewing.getUserId(),
					existingViewing.getShowId(),
					existingViewing.getEpisodeId(),
					requestBody.getUpdatedAt(),
					requestBody.getTimeCode()
			);

			return viewingRepository.save(updatedViewing);
		}

		Episode episode = episodeRepository.findOne(requestBody.getEpisodeId());
		if (episode != null) {
			Long showId = episode.getShowId();

			Viewing newViewing = Viewing.builder()
					.userId(userId)
					.showId(showId)
					.episodeId(requestBody.getEpisodeId())
					.updatedAt(requestBody.getUpdatedAt())
					.timeCode(requestBody.getTimeCode())
					.build();

			return viewingRepository.save(newViewing);
		}
		return null;
	}

	public List<RecentViewing> getRecentlyWatched(Long userId) {
		Map<Long, Episode> allEpisodes = episodeRepository.findAll()
				.stream()
				.collect(Collectors.toMap(
						episode -> episode.getId(),
						Function.identity()
				));

		Map<Long, Show> allShows = showRepository.findAll()
				.stream()
				.collect(Collectors.toMap(
						show -> show.getId(),
						Function.identity()
				));

		return viewingRepository.findByUserId(userId)
				.stream()
				.map(viewing -> RecentViewing.builder()
						.show(allShows.get(viewing.getShowId()))
						.episode(allEpisodes.get(viewing.getEpisodeId()))
						.updatedAt(viewing.getUpdatedAt())
						.timeCode(viewing.getTimeCode())
						.build())
				.collect(Collectors.toList());
	}

}
