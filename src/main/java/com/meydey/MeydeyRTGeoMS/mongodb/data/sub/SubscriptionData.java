package com.meydey.MeydeyRTGeoMS.mongodb.data.sub;

import com.meydey.MeydeyRTGeoMS.mongodb.data.chat.ChatBaseData;
import com.meydey.MeydeyRTGeoMS.mongodb.data.geo.GeoBaseData;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

@Document
public class SubscriptionData {

    @Id
    private String appointmentId;

    @Transient
    private List<WebSocketSession> activeSessions;

    private DestinationData destinationData;


    private ChatBaseData chatBaseData = new ChatBaseData();

    private GeoBaseData geoBaseData = new GeoBaseData();



    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public List<WebSocketSession> getActiveSessions() {
        return activeSessions;
    }

    public void setActiveSessions(List<WebSocketSession> activeSessions) {
        this.activeSessions = activeSessions;
    }

    public DestinationData getDestinationData() {
        return destinationData;
    }

    public void setDestinationData(DestinationData destinationData) {
        this.destinationData = destinationData;
    }

    public ChatBaseData getChatBaseData() {
        return chatBaseData;
    }

    public void setChatBaseData(ChatBaseData chatBaseData) {
        this.chatBaseData = chatBaseData;
    }

    public GeoBaseData getGeoBaseData() {
        return geoBaseData;
    }

    public void setGeoBaseData(GeoBaseData geoBaseData) {
        this.geoBaseData = geoBaseData;
    }
}
