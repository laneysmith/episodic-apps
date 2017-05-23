package com.example.episodicshows.shows;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "episodes")
@Getter
@Builder
@NoArgsConstructor(force = true)
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long showId;
    private Integer episodeNumber;
    private Integer seasonNumber;

    @Transient
    public String getTitle() {
        return String.format("S%d E%d", this.seasonNumber, this.episodeNumber);
    }

	@JsonCreator
	public Episode(
			@JsonProperty("id") Long id,
			@JsonProperty("showId") Long showId,
			@JsonProperty("episodeNumber") Integer episodeNumber,
			@JsonProperty("seasonNumber") Integer seasonNumber) {
		this.id = id;
		this.showId = showId;
		this.episodeNumber = episodeNumber;
		this.seasonNumber = seasonNumber;
	}
}
