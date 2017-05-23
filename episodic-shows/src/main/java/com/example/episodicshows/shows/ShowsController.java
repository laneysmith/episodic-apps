package com.example.episodicshows.shows;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shows")
public class ShowsController {

    private final ShowRepository showRepository;

    public ShowsController(
            ShowRepository showRepository
	) {
        this.showRepository = showRepository;
    }

    @PostMapping
    public Show createShow(@RequestBody Show show) {
        return showRepository.save(show);
    }

    @GetMapping
    public List<Show> getAllShows() {
        return showRepository.findAll();
    }

}