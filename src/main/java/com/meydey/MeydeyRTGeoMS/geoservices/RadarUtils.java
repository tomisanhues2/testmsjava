package com.meydey.MeydeyRTGeoMS.geoservices;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

public class RadarUtils {

    private static final String RADAR_API_URL = "https://api.radar.io/v1";
    private static final String RADAR_API_KEY = "prj_test_sk_93963f420a0d4eb2c55aa9f34d5c1df47a1859cb";

    public static boolean isInsideEnabledGeoFence(double latitude, double longitude) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", RADAR_API_KEY);
            headers.setContentType(MediaType.APPLICATION_JSON);

            URI url = UriComponentsBuilder.fromHttpUrl(RADAR_API_URL + "/context")
                    .queryParam("coordinates", latitude + "," + longitude)
                    .build()
                    .toUri();

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                String responseBody = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> responseMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, Object>>() {
                });

                Map<String, Object> context = (Map<String, Object>) responseMap.get("context");
                if (context == null) {
                    return false;
                }
                List<Map<String, Object>> geofences = (List<Map<String, Object>>) context.get("geofences");
                if (geofences == null || geofences.isEmpty()) {
                    return false;
                }
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
