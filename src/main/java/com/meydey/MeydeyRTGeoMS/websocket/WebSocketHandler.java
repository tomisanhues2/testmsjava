package com.meydey.MeydeyRTGeoMS.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meydey.MeydeyRTGeoMS.websocket.subscription.SubscriptionService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final SubscriptionService subscriptionService;

    public WebSocketHandler(ObjectMapper objectMapper, SubscriptionService subscriptionService) {
        this.objectMapper = objectMapper;
        this.subscriptionService = subscriptionService;
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String jsonMessage;
        JsonNode rootNode;


        try {
            jsonMessage = message.getPayload();
            rootNode = objectMapper.readTree(jsonMessage);
        } catch (Exception e) {
            // Handle invalid JSON
            System.out.println("Invalid JSON detected");
            return;
        }


        // UNCOMMENT THIS CODE TO ENABLE MESSAGE HASHING
//        // Extract the hash from the message
//        String receivedHash = rootNode.path("hash").asText();
//
//        // Reconstruct the message without the hash field for hashing
//        ((ObjectNode) rootNode).remove("hash");
//        String messageForHashing = rootNode.toString();
//
//        // Compute the hash of the message
//        String computedHash = computeHash(messageForHashing);
//
//        // Compare the received hash with the computed hash
//        if (!computedHash.equals(receivedHash)) {
//            // Handle invalid message
//            System.out.println("Invalid hash detected");
//            return;
//        }


        String action = rootNode.path("action").asText();
        JsonNode data = rootNode.path("data");
        String appointmentId = data.path("appointmentId").asText();
        if (appointmentId == null) {
            // Handle invalid appointmentId
            System.out.println("Invalid appointmentId detected");
            return;
        }
        switch (action) {
            case "subscribe":
                subscriptionService.subscribe(appointmentId, session);
                return;
            case "unsubscribe":
                subscriptionService.unsubscribe(appointmentId, session);
                return;
        }

        // Check if the user is subscribed to the provided appointmentId, if not, do not process the message
        if (!subscriptionService.isSubscribed(appointmentId, session)) {
            System.out.println("User is not subscribed to appointmentId: " + appointmentId);
            // Send a message to the user that they are not subscribed to the appointmentId
            session.sendMessage(new TextMessage("You are not subscribed to appointmentId: " + appointmentId));

            return;
        }

        ActionHandler handler = ActionHandlerFactory.getHandler(action);
        if (handler != null && !data.isMissingNode()) {
            handler.handleAction(session ,data.toString());
            subscriptionService.sendMessageToSubscribersExcludeSelf(session, appointmentId, message);
        } else {
            // Handle invalid action or missing data
        }
    }

    private String computeHash(String message) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(message.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes); // Implement bytesToHex to convert byte array to a hex string
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to compute hash", e);
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}