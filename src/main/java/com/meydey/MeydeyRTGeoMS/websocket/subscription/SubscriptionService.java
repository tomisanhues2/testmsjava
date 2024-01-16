package com.meydey.MeydeyRTGeoMS.websocket.subscription;

import com.meydey.MeydeyRTGeoMS.mongodb.data.sub.DestinationData;
import com.meydey.MeydeyRTGeoMS.mongodb.data.sub.SubscriptionData;
import com.meydey.MeydeyRTGeoMS.mongodb.data.sub.SubscriptionDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SubscriptionService {

    private final Map<String, SubscriptionData> subscriptions = new ConcurrentHashMap<>();
    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private SubscriptionDataRepository subscriptionDataRepository;

    public void subscribe(String id, WebSocketSession session) {
        subscriptions.compute(id, (key, subscriptionData) -> {
            if (subscriptionData == null) {
                subscriptionData = new SubscriptionData();
                subscriptionData.setAppointmentId(id);
                subscriptionData.setActiveSessions(new CopyOnWriteArrayList<>());

                // Make REST API call to get destination location
                DestinationData destinationData = fetchDestinationLocationFromApi(id);
                subscriptionData.setDestinationData(destinationData);

                subscriptionDataRepository.save(subscriptionData);
            }

            if (!subscriptionData.getActiveSessions().contains(session)) {
                subscriptionData.getActiveSessions().add(session);
            }
            try {
                session.sendMessage(new TextMessage("You are now subscribed to appointment " + id + "."));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            return subscriptionData;
        });
    }

    public void unsubscribe(String id, WebSocketSession session) {
        SubscriptionData subscriptionData = subscriptions.get(id);
        if (subscriptionData != null) {
            subscriptionData.getActiveSessions().remove(session);
        }
    }

    public List<WebSocketSession> getSubscribers(String id) {
        SubscriptionData subscriptionData = subscriptions.get(id);
        if (subscriptionData != null) {
            return subscriptionData.getActiveSessions();
        }
        return Collections.emptyList();
    }

    public SubscriptionData getSubscriptionInfo(String id) {
        return subscriptions.get(id);
    }


    private DestinationData fetchDestinationLocationFromApi(String id) {
        try {
            String apiUrl = "http://localhost/api/v1/appointments/" + id + "/";
            ResponseEntity<DestinationData> response = restTemplate.getForEntity(apiUrl, DestinationData.class);

            System.out.println("Response: " + response);

            DestinationData destinationData = response.getBody();
            if (destinationData != null) {
                return destinationData;
            } else {
                // Handle the case where the response body is null
                return new DestinationData();
            }
        } catch (Exception e) {
            // Handle exception
            return new DestinationData();
        }
    }

    public boolean isSubscribed(String id, WebSocketSession session) {
        SubscriptionData subscriptionData = subscriptions.get(id);
        if (subscriptionData != null) {
            return subscriptionData.getActiveSessions().contains(session);
        }
        return false;
    }

    public void sendMessageToSubscribersExcludeSelf(WebSocketSession senderSession, String id, TextMessage message) {
        SubscriptionData subscriptionData = subscriptions.get(id);
        if (subscriptionData != null) {
            for (WebSocketSession session : subscriptionData.getActiveSessions()) {
                if (session.isOpen() && !session.equals(senderSession)) {
                    try {
                        session.sendMessage(message);
                    } catch (IOException e) {
                        // Handle exception
                    }
                }
            }
        }
    }

    public void sendMessageToSubscribers(String id, TextMessage message) {
        SubscriptionData subscriptionData = subscriptions.get(id);
        if (subscriptionData != null) {
            for (WebSocketSession session : subscriptionData.getActiveSessions()) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(message);
                    } catch (IOException e) {
                        // Handle exception
                    }
                }
            }
        }
    }
}

