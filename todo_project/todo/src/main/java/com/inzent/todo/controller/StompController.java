package com.inzent.todo.controller;

import java.util.List;

import com.inzent.todo.stomp.ChatMessage;
import com.inzent.todo.vo.SocketVo;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

// import 생략...

@RequiredArgsConstructor
@Controller
public class StompController {

    private final SimpMessageSendingOperations messagingTemplate;

    // MessageMapping : websoket으로 들어오는 메시지 발행을 처리
    @MessageMapping("/ws/update") // 클라이언트에서 prefix로 /pub/chat/message로 발행요청!
    public void message(SocketVo id) {
        System.out.println(id.getName() + ":" + id.getSender());
        // System.out.println("CHAT MESSAGE.." + id[0].getName());
        // JOIN
        // if (ChatMessage.MessageType.ENTER.equals(message.getType()))
        // message.setMessage(message.getSender() + "님이 입장하셨습니다.");

        // SendTo..?
        messagingTemplate.convertAndSend("/sub/project/room/" + id.getName(), id.getSender());
    }
}