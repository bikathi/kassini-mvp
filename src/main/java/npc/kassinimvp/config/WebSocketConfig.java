package npc.kassinimvp.config;

import npc.kassinimvp.listener.WebSockectChannelInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@EnableWebSocket
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Autowired
    WebSockectChannelInterceptor channelInterceptor;
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		// for normal websocket connections
		registry.addEndpoint("/ws-registry").setAllowedOriginPatterns("*");

		// for other web-socket connections that support SockJS fallback
		registry.addEndpoint("/ws-registry").setAllowedOriginPatterns("*").withSockJS();
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableStompBrokerRelay("/queue")
			.setRelayHost("localhost")
			.setRelayPort(61613)
			.setSystemLogin("admin")
			.setSystemPasscode("admin")
			.setClientLogin("admin")
			.setClientPasscode("admin");
		
		registry.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(channelInterceptor);
	}
	
	
}
