package com.example.episodicshows.viewings;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
public class AmqpListener implements RabbitListenerConfigurer {

	private final ViewingsService viewingsService;
	private final ObjectMapper objectMapper;

	public AmqpListener(
			ViewingsService viewingsService,
			ObjectMapper objectMapper) {
		assert viewingsService != null;
		assert objectMapper != null;
		this.viewingsService = viewingsService;
		this.objectMapper = objectMapper;
	}

	@RabbitListener(queues = "episodic-progress")
	public void receiveMessage(final RabbitViewing viewing) {
		System.out.println("************************************************");
		System.out.println(viewing.toString());
		System.out.println("************************************************");

		viewingsService.createOrUpdateViewing(
				viewing.getUserId(),
				Viewing.builder()
						.episodeId(viewing.getEpisodeId())
						.updatedAt(viewing.getCreatedAt())
						.timeCode(viewing.getOffset())
						.build());
	}

	@Bean
	public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
		DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
		MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
		messageConverter.setObjectMapper(objectMapper);
		factory.setMessageConverter(messageConverter);
		return factory;
	}

	@Override
	public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
		registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
	}

	public static class HelloMessage {
		public String hello;

		public String getHello() {
			return hello;
		}

		public void setHello(String hello) {
			this.hello = hello;
		}

		@Override
		public String toString() {
			return "HelloMessage{" +
					"hello='" + hello + '\'' +
					'}';
		}
	}


}
