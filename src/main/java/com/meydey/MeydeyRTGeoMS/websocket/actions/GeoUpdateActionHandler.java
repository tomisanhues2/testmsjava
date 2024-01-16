package com.meydey.MeydeyRTGeoMS.websocket.actions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meydey.MeydeyRTGeoMS.geoservices.GeoUtils;
import com.meydey.MeydeyRTGeoMS.mongodb.data.geo.*;
import com.meydey.MeydeyRTGeoMS.mongodb.data.sub.DestinationData;
import com.meydey.MeydeyRTGeoMS.mongodb.data.sub.SubscriptionData;
import com.meydey.MeydeyRTGeoMS.mongodb.data.sub.SubscriptionDataRepository;
import com.meydey.MeydeyRTGeoMS.websocket.ActionHandler;
import com.meydey.MeydeyRTGeoMS.websocket.subscription.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.Date;
import java.util.Optional;

@Component
public class GeoUpdateActionHandler implements ActionHandler {
    private SubscriptionDataRepository subscriptionDataRepository;
    private SubscriptionService subscriptionService;

    @Autowired
    public GeoUpdateActionHandler(SubscriptionDataRepository subscriptionDataRepository, SubscriptionService subscriptionService) {
        this.subscriptionDataRepository = subscriptionDataRepository;
        this.subscriptionService = subscriptionService;
    }

    public void handleAction(WebSocketSession session, String jsonData) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            GeoRequestData geoRequest = objectMapper.readValue(jsonData, GeoRequestData.class);

            Optional<SubscriptionData> existingSubscriptionDataOpt = subscriptionDataRepository.findById(geoRequest.getAppointmentId());


            if (existingSubscriptionDataOpt.isPresent()) {
                SubscriptionData subscriptionData = existingSubscriptionDataOpt.get();

                GeoData newGeoData = new GeoData();
                newGeoData.setUserId(geoRequest.getUserId());
                newGeoData.setLatitude(geoRequest.getLatitude());
                newGeoData.setLongitude(geoRequest.getLongitude());
                newGeoData.setTimestamp(new Date());

                subscriptionData.getGeoBaseData().getGeoData().add(newGeoData);
                subscriptionDataRepository.save(subscriptionData);
            } else {

            }
            SubscriptionData subscriptionData = this.subscriptionService.getSubscriptionInfo(geoRequest.getAppointmentId());

            if (subscriptionData == null) {
                return;
            }

            // Calculate how far the user is from the destination
            DestinationData.Location destination = subscriptionData.getDestinationData().getLocation();

            double distance = GeoUtils.calculateDistanceInMeters(geoRequest.getLatitude(), geoRequest.getLongitude(),
                    destination.getLatitude(), destination.getLongitude());
            subscriptionService.sendMessageToSubscribers(geoRequest.getAppointmentId(), new TextMessage(jsonData.getBytes()));

            System.out.println("Distance: " + distance);


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public static class GeoRequestData {
        private String appointmentId;
        private String userId;
        private double latitude;
        private double longitude;

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

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }
    }

}
