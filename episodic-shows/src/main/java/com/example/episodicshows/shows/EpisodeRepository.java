package com.example.episodicshows.shows;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EpisodeRepository extends CrudRepository<Episode, Long> {
    List<Episode> findByShowId(Long showId);
}
