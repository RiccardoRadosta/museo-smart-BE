package it.CampiFlegrei.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EarthquakeDTO {

    private int id;
    private String type;
    private String area;
    private String date;
    private String printdate;
    private double epoch;
    private String level;
    private String classType;
    private List<MagnitudeDTO> magnitudos;
    private String htmlMags;
    private String splitMags;
    private LocationDTO location;
    private int year;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MagnitudeDTO {
        private String type;
        private int id;
        private double value;
        private String method;
        private double error;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LocationDTO {
        private int id;
        private double latitude;
        private double longitude;
        private double depth;
        private String stringLatitude;
        private String stringLongitude;
        private String quality;
    }
}
