package com.meydey.MeydeyRTGeoMS.websocket;

import com.meydey.MeydeyRTGeoMS.websocket.actions.ChatSendActionHandler;
import com.meydey.MeydeyRTGeoMS.websocket.actions.GeoUpdateActionHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ActionHandlerFactory implements ApplicationContextAware {

    private static ApplicationContext context;
    private static final Map<String, Class<? extends ActionHandler>> handlerTypes = new HashMap<>();

    static {
        handlerTypes.put("geo_send", GeoUpdateActionHandler.class);
        handlerTypes.put("chat_send", ChatSendActionHandler.class);
        // Add other handlers here
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ActionHandler getHandler(String action) {
        Class<? extends ActionHandler> handlerClass = handlerTypes.get(action);
        if (handlerClass != null) {
            return context.getBean(handlerClass);
        }
        return null;
    }
}