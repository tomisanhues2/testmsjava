package com.meydey.MeydeyRTGeoMS.mongodb.data.chat;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Date;

public class ChatMessageData {

    private String userId;
    private String messageContent;

    private Date timestamp;

    public ChatMessageData(String userId, String messageContent, Date timestamp) {
        this.userId = userId;
        this.messageContent = messageContent;
        this.timestamp = timestamp;
    }

    public ChatMessageData() {

    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
