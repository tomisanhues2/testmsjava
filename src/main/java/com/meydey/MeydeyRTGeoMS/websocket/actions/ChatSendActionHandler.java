package com.meydey.MeydeyRTGeoMS.websocket.actions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meydey.MeydeyRTGeoMS.mongodb.data.chat.ChatDataService;
import com.meydey.MeydeyRTGeoMS.mongodb.data.chat.ChatMessageData;
import com.meydey.MeydeyRTGeoMS.websocket.ActionHandler;
import com.meydey.MeydeyRTGeoMS.websocket.data.ResponseCategory;
import com.meydey.MeydeyRTGeoMS.websocket.data.ResponseCode;
import com.meydey.MeydeyRTGeoMS.websocket.data.ResponseData;
import com.meydey.MeydeyRTGeoMS.websocket.subscription.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Component
public class ChatSendActionHandler implements ActionHandler {

    private final SubscriptionService subscriptionService;
    private final ChatDataService chatDataService;

    @Autowired
    public ChatSendActionHandler(SubscriptionService subscriptionService, ChatDataService chatDataService) {
        this.subscriptionService = subscriptionService;
        this.chatDataService = chatDataService;
    }


    @Override
    public void handleAction(WebSocketSession session, String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ChatRequestData chatRequest = objectMapper.readValue(jsonData, ChatRequestData.class);

            String appointmentId = chatRequest.getAppointmentId();
            String userId =  chatRequest.getUserId();
            String message = chatRequest.getMessage();

            ChatMessageData chatData = chatDataService.sendMessage(appointmentId, userId, message);


            // Construct the output message object

            ResponseData responseData = new ResponseData();
            responseData.setCode(ResponseCode.SUCCESS.getCode());
            responseData.setCategory(ResponseCategory.CHAT.getCategory());
            responseData.setMessage(ResponseCode.SUCCESS.getMessage());
            responseData.setData(chatData);


            // Send the message to all active sessions
            subscriptionService.sendMessageToSubscribers(appointmentId, responseData);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class ChatRequestData {
        private String appointmentId;
        private String userId;
        private String message;

        public String getAppointmentId() {
            return appointmentId;
        }

        public void setAppointmentId(String appointmentId) {
            this.appointmentId = appointmentId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


}

