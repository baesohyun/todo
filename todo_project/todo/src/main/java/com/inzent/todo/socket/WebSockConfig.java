// package com.inzent.todo.socket;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.socket.config.annotation.EnableWebSocket;
// import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
// import
// org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

// import lombok.RequiredArgsConstructor;

// @RequiredArgsConstructor
// @Configuration
// @EnableWebSocket
// public class WebSockConfig implements WebSocketConfigurer {

// private final WebSockHandler webSocketHandler;

// @Override
// public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
// registry.addHandler(webSocketHandler, "/ws/chat").setAllowedOrigins("*");

// }

// }