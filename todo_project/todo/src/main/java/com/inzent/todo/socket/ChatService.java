// package com.inzent.todo.socket;

// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.LinkedHashMap;
// import java.util.List;
// import java.util.Map;
// import java.util.UUID;

// import javax.annotation.PostConstruct;

// import com.fasterxml.jackson.databind.ObjectMapper;

// import org.springframework.stereotype.Service;
// import org.springframework.web.socket.TextMessage;
// import org.springframework.web.socket.WebSocketSession;

// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;

// @Slf4j
// @RequiredArgsConstructor
// @Service
// public class ChatService {

// private final ObjectMapper objectMapper;
// private Map<String, ChatRoom> chatRooms; // 서버에 생성된 모든 채팅방 정보를 모아둔 구조체.
// // 실제구현하게되면 채팅방 정보를 외부저장소(DB) 같은 곳에 보관해야할듯..

// @PostConstruct
// private void init() {
// chatRooms = new LinkedHashMap<>();
// }

// // Map에 담긴 채팅방 정보 조회 -> 실제는 DB에서 조회해야겟지..?
// public List<ChatRoom> findAllRoom() {
// return new ArrayList<>(chatRooms.values());
// }

// public ChatRoom findRoomById(String roomId) {
// return chatRooms.get(roomId);
// }

// // 채팅방이름 생성! UUID -> 대충 프로젝트 아이디로 해도 무방할듯.
// public ChatRoom createRoom(String name) {
// String randomId = UUID.randomUUID().toString();
// ChatRoom chatRoom = ChatRoom.builder().roomId(randomId).name(name).build();
// chatRooms.put(randomId, chatRoom);
// return chatRoom;
// }

// // 메시지발송 : 지정한 WebSocket세션에 메세지 발송
// public <T> void sendMessage(WebSocketSession session, T message) {
// try {
// session.sendMessage(new
// TextMessage(objectMapper.writeValueAsString(message)));
// } catch (IOException e) {
// log.error(e.getMessage(), e);
// }
// }
// }