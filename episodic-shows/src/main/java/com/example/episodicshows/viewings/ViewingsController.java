package com.example.episodicshows.viewings;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class ViewingsController {

	private ViewingsService viewingsService;

	public ViewingsController(
			ViewingsService viewingsService
	) {
		this.viewingsService = viewingsService;
	}

	@PatchMapping("/{userId}/viewings")
	public Viewing createOrUpdateViewing(
			@RequestBody Viewing viewing,
			@PathVariable Long userId
	) {
		return viewingsService.createOrUpdateViewing(userId, viewing);
	}

	@GetMapping("/{userId}/recently-watched")
	public List<RecentViewing> getRecentlyWatched(
			@PathVariable Long userId
	) {
		return viewingsService.getRecentlyWatched(userId);
	}

}