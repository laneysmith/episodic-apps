package com.example.episodicshows.viewings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "viewings")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Viewing {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@JsonIgnore
	private long userId;

	@JsonIgnore
	private long showId;

	private long episodeId;
	private LocalDateTime updatedAt;
	private int timeCode;

}
