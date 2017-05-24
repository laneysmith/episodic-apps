package com.example.episodicevents.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class EventsControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	EventRepository eventRepository;

	@Autowired
	ObjectMapper objectMapper;

	private String type;
	private LocalDateTime timestamp;

	@Before
	public void setup() {
		eventRepository.deleteAll();
		timestamp = LocalDateTime.now();
	}

	@Test
	public void testGetRecent() throws Exception {
		for (int i = 0; i < 21; i++) {
			eventRepository.save(new PlayEvent());
		}

		MockHttpServletRequestBuilder request = get("/recent")
				.contentType(MediaType.APPLICATION_JSON);

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.length()", equalTo(20)));
	}


	@Test
	public void testPost_withPlayEvent() throws Exception {
		type = "play";

		String event = eventBuilder(type, timestamp, singletonMap("offset", 2));

		MockHttpServletRequestBuilder request = postEvent(event);

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.type", equalTo(type)))
				.andExpect(jsonPath("$.userId", equalTo(1)))
				.andExpect(jsonPath("$.showId", equalTo(2)))
				.andExpect(jsonPath("$.episodeId", equalTo(3)))
				.andExpect(jsonPath("$.createdAt", equalTo(timestamp.truncatedTo(ChronoUnit.MILLIS).toString())))
				.andExpect(jsonPath("$.data.offset", equalTo(2)));
	}

	@Test
	public void testPost_withFastForwardEvent() throws Exception {
		type = "fastForward";

		Map<String, Object> data = new HashMap<String, Object>(){
			{
				put("startOffset", 1);
				put("endOffset", 2);
				put("speed", 1.5);
			}
		};

		String event = eventBuilder(type, timestamp, data);

		MockHttpServletRequestBuilder request = postEvent(event);

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.type", equalTo(type)))
				.andExpect(jsonPath("$.userId", equalTo(1)))
				.andExpect(jsonPath("$.showId", equalTo(2)))
				.andExpect(jsonPath("$.episodeId", equalTo(3)))
				.andExpect(jsonPath("$.createdAt", equalTo(timestamp.truncatedTo(ChronoUnit.MILLIS).toString())))
				.andExpect(jsonPath("$.data.startOffset", equalTo(data.get("startOffset"))))
				.andExpect(jsonPath("$.data.endOffset", equalTo(2)))
				.andExpect(jsonPath("$.data.speed", equalTo(1.5)));
	}

	@Test
	public void testPost_withPauseEvent() throws Exception {
		type = "pause";

		String event = eventBuilder(type, timestamp, singletonMap("offset", 2));

		MockHttpServletRequestBuilder request = postEvent(event);

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.type", equalTo(type)))
				.andExpect(jsonPath("$.userId", equalTo(1)))
				.andExpect(jsonPath("$.showId", equalTo(2)))
				.andExpect(jsonPath("$.episodeId", equalTo(3)))
				.andExpect(jsonPath("$.createdAt", equalTo(timestamp.truncatedTo(ChronoUnit.MILLIS).toString())))
				.andExpect(jsonPath("$.data.offset", equalTo(2)));
	}

	@Test
	public void testPostEvent_withProgress() throws Exception {
		type = "progress";

		String event = eventBuilder(type, timestamp, singletonMap("offset", 2));

		MockHttpServletRequestBuilder request = postEvent(event);

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.type", equalTo(type)))
				.andExpect(jsonPath("$.userId", equalTo(1)))
				.andExpect(jsonPath("$.showId", equalTo(2)))
				.andExpect(jsonPath("$.episodeId", equalTo(3)))
				.andExpect(jsonPath("$.createdAt", equalTo(timestamp.truncatedTo(ChronoUnit.MILLIS).toString())))
				.andExpect(jsonPath("$.data.offset", equalTo(2)));
	}

	@Test
	public void testPost_withRewindEvent() throws Exception {
		type = "rewind";

		Map<String, Object> data = new HashMap<String, Object>(){
			{
				put("startOffset", 1);
				put("endOffset", 2);
				put("speed", 1.5);
			}
		};
		String event = eventBuilder(type, timestamp, data);

		MockHttpServletRequestBuilder request = postEvent(event);

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.type", equalTo(type)))
				.andExpect(jsonPath("$.userId", equalTo(1)))
				.andExpect(jsonPath("$.showId", equalTo(2)))
				.andExpect(jsonPath("$.episodeId", equalTo(3)))
				.andExpect(jsonPath("$.createdAt", equalTo(timestamp.truncatedTo(ChronoUnit.MILLIS).toString())))
				.andExpect(jsonPath("$.data.startOffset", equalTo(1)))
				.andExpect(jsonPath("$.data.endOffset", equalTo(2)))
				.andExpect(jsonPath("$.data.speed", equalTo(1.5)));
	}

	@Test
	public void testPost_withScrubEvent() throws Exception {
		type = "scrub";
		Map<String, Object> data = new HashMap<String, Object>(){
			{
				put("startOffset", 1);
				put("endOffset", 2);
			}
		};
		String event = eventBuilder(type, timestamp, data);

		MockHttpServletRequestBuilder request = postEvent(event);

		mvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.type", equalTo(type)))
				.andExpect(jsonPath("$.userId", equalTo(1)))
				.andExpect(jsonPath("$.showId", equalTo(2)))
				.andExpect(jsonPath("$.episodeId", equalTo(3)))
				.andExpect(jsonPath("$.createdAt", equalTo(timestamp.truncatedTo(ChronoUnit.MILLIS).toString())))
				.andExpect(jsonPath("$.data.startOffset", equalTo(1)))
				.andExpect(jsonPath("$.data.endOffset", equalTo(2)));
	}

	private MockHttpServletRequestBuilder postEvent(String requestBody) {
		return post("/")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestBody);
	}

	private String eventBuilder(String type, LocalDateTime timestamp, Map data) throws JsonProcessingException {
		Map map = new HashMap<String, Object>() {
			{
				put("type", type);
				put("userId", 1L);
				put("showId", 2L);
				put("episodeId", 3L);
				put("createdAt", timestamp);
				put("data", data);
			}
		};

		return objectMapper.writeValueAsString(map);
	}
}