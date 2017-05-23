package com.example.episodicshows.shows;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "shows")
@Getter
@Builder
@NoArgsConstructor
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @JsonCreator
	public Show(
			@JsonProperty("id") Long id,
			@JsonProperty("name") String name) {
		this.id = id;
		this.name = name;
	}
}