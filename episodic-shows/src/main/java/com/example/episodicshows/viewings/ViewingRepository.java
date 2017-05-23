package com.example.episodicshows.viewings;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ViewingRepository extends CrudRepository<Viewing, Long> {
	List<Viewing> findByUserId(Long userId);

	Viewing findByUserIdAndEpisodeId(long userId, long episodeId);
}