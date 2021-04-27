// package com.inzent.todo.socket;

// import java.util.HashSet;
// import java.util.Set;

// import org.springframework.web.socket.WebSocketSession;

// import lombok.Builder;
// import lombok.Getter;

// @Getter
// public class ChatRoom {
// private String roomId;
// private String name;
// private Set<WebSocketSession> sessions = new HashSet<>();
// // 입장한 클라이언트 session 정보를 리스트로 보관

// @Builder
// public ChatRoom(String roomId, String name) {
// // 생성자?
// this.roomId = roomId;
// this.name = name;
// }

// public void handleActions(WebSocketSession session, ChatMessage chatMessage,
// ChatService chatService) {
// if (chatMessage.getType().equals(ChatMessage.MessageType.ENTER)) {
// sessions.add(session);
// chatMessage.setMessage(chatMessage.getSender() + "님이 입장했습니다.");
// }
// sendMessage(chatMessage, chatService);
// }

// public <T> void sendMessage(T message, ChatService chatService) {
// sessions.parallelStream().forEach(session -> chatService.sendMessage(session,
// message));
// }
// }