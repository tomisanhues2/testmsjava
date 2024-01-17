package com.meydey.MeydeyRTGeoMS.websocket.data;

public enum ResponseCategory {

    CHAT("CHAT"),
    GEO("GEO"),
    SYSTEM("SYSTEM");

    private final String category;

    private ResponseCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }
}
