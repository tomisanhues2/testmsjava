package com.meydey.MeydeyRTGeoMS.websocket.data;

public enum ResponseCode {

    SUCCESS("M200", "Operation successful"),
    INVALID_JSON("M400", "Invalid JSON"),
    INVALID_REQUEST("M401", "Invalid request"),
    SERVER_ERROR("M500", "Server error");

    private String code;
    private String message;

    private ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
