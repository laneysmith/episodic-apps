package com.example.episodicshows.shows;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shows")
public class ShowsController {

    private final ShowRepository showRepository;

    public ShowsController(ShowRepository showRepository) {
        this.showRepository = showRepository;
    }

    @PostMapping
    public Show createShow(@RequestBody Show show) {
        return this.showRepository.save(show);
    }

    @GetMapping
    public Iterable<Show> getAllShows() {
        return this.showRepository.findAll();
    }
}