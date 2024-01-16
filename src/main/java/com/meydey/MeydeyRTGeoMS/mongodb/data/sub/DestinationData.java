package com.meydey.MeydeyRTGeoMS.mongodb.data.sub;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DestinationData {
    private Location location;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public static class Location {
        private String id;

        @JsonProperty("first_line")
        private String firstLine;

        @JsonProperty("second_line")
        private String secondLine;
        private double latitude;
        private double longitude;

        // Getters and setters


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstLine() {
            return firstLine;
        }

        public void setFirstLine(String firstLine) {
            this.firstLine = firstLine;
        }

        public String getSecondLine() {
            return secondLine;
        }

        public void setSecondLine(String secondLine) {
            this.secondLine = secondLine;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        @Override
        public String toString() {
            return "Location{" +
                    "id='" + id + '\'' +
                    ", firstLine='" + firstLine + '\'' +
                    ", secondLine='" + secondLine + '\'' +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AddressData{" +
                "location=" + location +
                '}';
    }
}