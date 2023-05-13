package com.effourt.calenkit.config;

import com.effourt.calenkit.util.websockethandler.AlarmHandler;
import com.effourt.calenkit.util.websockethandler.ScheduleHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@EnableWebSocket
@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(alarmHandler(), "/alarm")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .withSockJS();

        registry.addHandler(scheduleHandler(), "/schedule")
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                .withSockJS();
    }
    @Bean
    public WebSocketHandler alarmHandler() {
        return new AlarmHandler();
    }

    @Bean
    public WebSocketHandler scheduleHandler() {
        return new ScheduleHandler();
    }

}
