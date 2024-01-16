package com.meydey.MeydeyRTGeoMS.mongodb.data.chat;

import com.meydey.MeydeyRTGeoMS.mongodb.data.sub.SubscriptionData;
import com.meydey.MeydeyRTGeoMS.mongodb.data.sub.SubscriptionDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class ChatDataService {

    @Autowired
    private SubscriptionDataRepository subscriptionDataRepository;

    public ChatMessageData sendMessage(String appointmentId, String userId, String messageContent) {
        SubscriptionData subscriptionData = subscriptionDataRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Subscription data not found"));

        ChatMessageData message = new ChatMessageData(userId, messageContent, new Date());
        subscriptionData.getChatBaseData().getMessages().add(message);
        subscriptionDataRepository.save(subscriptionData);

        return message;
    }
}
