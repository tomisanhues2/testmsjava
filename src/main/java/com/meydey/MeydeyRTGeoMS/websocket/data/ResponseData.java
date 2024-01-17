package com.meydey.MeydeyRTGeoMS.websocket.data;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ResponseData {

    private String code;
    private String message;

    private String category;
    private Object data;

    public ResponseData() {
    }


    public String getCode() {
        return code;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (Exception e) {
            return "";
        }
    }




}
