package com.hobbyzhub.chatservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws-registry").setAllowedOriginPatterns("*").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableStompBrokerRelay("/queue", "/topic")
			.setRelayHost("localhost")
			.setRelayPort(61613)
			.setSystemLogin("admin")
			.setSystemPasscode("admin")
			.setClientLogin("admin")
			.setClientPasscode("admin");
		
		registry.setApplicationDestinationPrefixes("/app", "/queue", "/topic");
	}
	
}
