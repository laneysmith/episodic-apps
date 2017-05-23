package com.example.episodicshows.shows;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/shows")
public class EpisodesController {

    private final EpisodeRepository episodeRepository;

    public EpisodesController(
			EpisodeRepository episodeRepository
	) {
        this.episodeRepository = episodeRepository;
    }

    @PostMapping("/{showId}/episodes")
    public Episode createEpisodeForShowId(
            @RequestBody HashMap<String, Integer> map,
            @PathVariable Long showId
    ) {
		Episode newEpisode = Episode.builder()
				.showId(showId)
				.episodeNumber(map.get("episodeNumber"))
				.seasonNumber(map.get("seasonNumber"))
				.build();

		return episodeRepository.save(newEpisode);
    }

    @GetMapping("/{showId}/episodes")
    public List<Episode> getAllEpisodesByShowId(
            @PathVariable Long showId
    ) {
        return episodeRepository.findByShowId(showId);
    }
}