package com.meydey.MeydeyRTGeoMS.rest;

import com.meydey.MeydeyRTGeoMS.geoservices.RadarUtils;
import com.meydey.MeydeyRTGeoMS.rest.geofence.GeoFenceCheckResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeoFenceController {


    @GetMapping("/in-geofence")
    public GeoFenceCheckResponse isInGeoFence(@RequestParam(value = "latitude") double latitude,
                                               @RequestParam(value = "longitude") double longitude) {

        boolean isInside = RadarUtils.isInsideEnabledGeoFence(latitude, longitude);

        return new GeoFenceCheckResponse(isInside);
    }
}
