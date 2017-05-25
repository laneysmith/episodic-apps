package com.example.episodicshows.viewings;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RabbitViewing {
	private long userId;
	private long episodeId;
	private LocalDateTime createdAt;
	private int offset;
}
