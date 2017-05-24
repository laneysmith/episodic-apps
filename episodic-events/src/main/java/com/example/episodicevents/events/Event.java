package com.example.episodicevents.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type",
		visible = true)
@JsonSubTypes({
		@JsonSubTypes.Type(value = FastForwardEvent.class, name = "fastForward"),
		@JsonSubTypes.Type(value = PauseEvent.class, name = "pause"),
		@JsonSubTypes.Type(value = PlayEvent.class, name = "play"),
		@JsonSubTypes.Type(value = ProgressEvent.class, name = "progress"),
		@JsonSubTypes.Type(value = RewindEvent.class, name = "rewind"),
		@JsonSubTypes.Type(value = ScrubEvent.class, name = "scrub")
})
public abstract class Event {

	@Id
	public String id;

	public String type;
	public Long userId;
	public Long showId;
	public Long episodeId;
	public LocalDateTime createdAt;

}