package com.inzent.todo.stomp;

// import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatRoom {
    // pub/sub 방식을 이용해서 웹소켓 세션관리 필요 x
    // 클라이언트로 발송구현도 알아서 해줌! (sendMessage필요x)
    private String roomId;
    private String name;

    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.roomId = name;// UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }

}