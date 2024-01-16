package com.meydey.MeydeyRTGeoMS.mongodb.data.geo;



import java.util.ArrayList;
import java.util.List;

public class GeoBaseData {

    List<GeoData> geoData = new ArrayList<>();

    public GeoBaseData() {
    }

    public List<GeoData> getGeoData() {
        return geoData;
    }

    public void setGeoData(List<GeoData> geoData) {
        this.geoData = geoData;
    }
}
