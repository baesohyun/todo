// package com.inzent.todo.controller;

// import java.util.List;

// import com.inzent.todo.socket.ChatRoom;
// import com.inzent.todo.socket.ChatService;

// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;

// import lombok.RequiredArgsConstructor;

// @RequiredArgsConstructor
// @RestController
// @RequestMapping("/chat2")
// public class ChatController {

// private final ChatService chatService;

// @PostMapping
// public ChatRoom createRoom(@RequestBody String name) {
// System.out.println("createChatRomm..:" + name);
// return chatService.createRoom(name);
// }

// @GetMapping
// public List<ChatRoom> findAllRoom() {
// return chatService.findAllRoom();
// }
// }