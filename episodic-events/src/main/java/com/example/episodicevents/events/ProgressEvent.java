package com.example.episodicevents.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProgressEvent extends Event {
	private Data data;

	@Getter
	public static class Data {
		private Integer offset;
	}
}