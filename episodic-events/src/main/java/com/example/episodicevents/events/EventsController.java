package com.example.episodicevents.events;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class EventsController {

	private final EventRepository eventRepository;

	public EventsController(EventRepository eventRepository) {
		this.eventRepository = eventRepository;
	}

	@GetMapping("/recent")
	public Iterable<Event> getRecentEvents() {
		return eventRepository.findAll(new PageRequest(0, 20, new Sort(Sort.Direction.DESC, "createdAt"))).getContent();
	}

	@PostMapping
	public Event createEvent(@RequestBody Event event) {
		return eventRepository.save(event);
	}
}