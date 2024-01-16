package com.meydey.MeydeyRTGeoMS.mongodb.data.chat;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public class ChatBaseData {

    private List<ChatMessageData> messages = new ArrayList<>();

    public ChatBaseData() {

    }

    public List<ChatMessageData> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessageData> messages) {
        this.messages = messages;
    }
}

