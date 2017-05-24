package com.example.episodicevents.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FastForwardEvent extends Event {
	private Data data;

	@Getter
	public static class Data {
		private Integer startOffset;
		private Integer endOffset;
		private Float speed;
	}
}