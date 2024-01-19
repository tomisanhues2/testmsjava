package com.meydey.MeydeyRTGeoMS.rest.geofence;

public class GeoFenceCheckResponse {

    private boolean isInside;

    public GeoFenceCheckResponse(boolean isInside) {
        this.isInside = isInside;
    }

    public boolean isInside() {
        return isInside;
    }

    public void setInside(boolean inside) {
        isInside = inside;
    }
}
