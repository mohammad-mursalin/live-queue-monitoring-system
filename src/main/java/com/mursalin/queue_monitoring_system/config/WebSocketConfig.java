package com.mursalin.queue_monitoring_system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Organized channels
        config.enableSimpleBroker(
                "/topic/queue-updates",  // User-facing
                "/topic/admin",          // Admin dashboard
                "/topic/announcements"   // Global broadcasts
        );
        config.setApplicationDestinationPrefixes("/app");
        config.setUserDestinationPrefix("/user"); // For private messages
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS()
                .setHeartbeatTime(60_000); // Prevent timeouts
    }




}
