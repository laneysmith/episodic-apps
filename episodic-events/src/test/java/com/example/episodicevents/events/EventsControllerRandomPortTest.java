package com.example.episodicevents.events;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.anyList;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventsControllerRandomPortTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	MongoTemplate mongoTemplate;

	@Before
	public void clearDatabase() {
		mongoTemplate.getCollection("episodic-events").drop();
		mongoTemplate.createCollection("episodic-events");

		PlayEvent event = new PlayEvent();
		mongoTemplate.insert(event, "episodic-events");

	}

//	@Test
//	public void testGetRecent_returnsOnly() throws Exception {
//		for (int i = 0; i < 21; i++) {
//			eventRepository.save(new PlayEvent());
//		}
//
//		MockHttpServletRequestBuilder request = get("/recent")
//				.contentType(MediaType.APPLICATION_JSON);
//
//		mvc.perform(request)
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.length()", equalTo(20)));
//	}


	@Test
	public void exampleTest() {
		String body = this.restTemplate.getForObject("/recent", String.class);
		assertThat(body, equalTo(anyList().toString()));
		assertThat(body.length(), equalTo(1));
	}

}