package com.inzent.todo.controller;

import com.inzent.todo.stomp.ChatMessage;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SocketController {

    // /receive : 메세지를 수신할 endpoint
    // @MessageMapping("/receive")

    // /send로 메세지를 반환
    // @SendTo("/send")

    // public ChatMessage SocketHandler(ChatMessage chatMessage) {
    // }

}