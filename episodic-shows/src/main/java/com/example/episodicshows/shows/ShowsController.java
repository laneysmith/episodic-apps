package com.example.episodicshows.shows;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/shows")
public class ShowsController {

    private final ShowRepository showRepository;
    private final EpisodeRepository episodeRepository;

    public ShowsController(
            ShowRepository showRepository,
			EpisodeRepository episodeRepository
	) {
        this.showRepository = showRepository;
        this.episodeRepository = episodeRepository;
    }

    @PostMapping
    public Show createShow(@RequestBody Show show) {
        return showRepository.save(show);
    }

    @GetMapping
    public Iterable<Show> getAllShows() {
        return showRepository.findAll();
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
    public Iterable<Episode> getAllEpisodesByShowId(
            @PathVariable Long showId
    ) {
        return episodeRepository.findByShowId(showId);
    }
}