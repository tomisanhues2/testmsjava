package com.meydey.MeydeyRTGeoMS.websocket;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONObject;
import org.springframework.web.socket.WebSocketSession;

public interface ActionHandler {
    void handleAction(WebSocketSession session, String jsonData);
}
