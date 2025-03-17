package it.CampiFlegrei.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import it.CampiFlegrei.dto.EarthquakeDTO;
import it.CampiFlegrei.dto.EarthquakeDTO.MagnitudeDTO;
import it.CampiFlegrei.dto.EarthquakeDTO.LocationDTO;

import javax.transaction.Transactional;

@Repository
@Transactional
public class EarthquakeDAOImpl implements EarthquakeDAO {

    private static final String URL = "https://terremoti.ov.ingv.it/gossip/flegrei/events.json";

    @Override
    public List<EarthquakeDTO> getEarthquakeData() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(URL, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            String jsonString = response.getBody();

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonNode = objectMapper.readTree(jsonString);

                List<EarthquakeDTO> earthquakes = new ArrayList<>();
                if (jsonNode.isArray()) {
                    List<JsonNode> nodes = new ArrayList<>();
                    jsonNode.forEach(nodes::add);

                    // Parallelizzazione del parsing
                    earthquakes = nodes.parallelStream().limit(2000).map(node -> {
                        EarthquakeDTO dto = new EarthquakeDTO();
                        dto.setId(getJsonNodeAsInt(node, "id"));
                        dto.setType(getJsonNodeAsText(node, "type"));
                        dto.setArea(getJsonNodeAsText(node, "area"));
                        dto.setDate(getJsonNodeAsText(node, "date"));
                        dto.setPrintdate(getJsonNodeAsText(node, "printdate"));
                        dto.setEpoch(getJsonNodeAsDouble(node, "epoch"));
                        dto.setLevel(getJsonNodeAsText(node, "level"));
                        dto.setClassType(getJsonNodeAsText(node, "class"));
                        dto.setHtmlMags(getJsonNodeAsText(node, "html_mags"));
                        dto.setSplitMags(getJsonNodeAsText(node, "split_mags"));
                        dto.setYear(getJsonNodeAsInt(node, "year"));

                        List<MagnitudeDTO> magnitudos = new ArrayList<>();
                        if (node.has("magnitudos")) {
                            for (JsonNode magNode : node.get("magnitudos")) {
                                MagnitudeDTO magDto = new MagnitudeDTO();
                                magDto.setType(getJsonNodeAsText(magNode, "type"));
                                magDto.setId(getJsonNodeAsInt(magNode, "id"));
                                magDto.setValue(getJsonNodeAsDouble(magNode, "value"));
                                magDto.setMethod(getJsonNodeAsText(magNode, "method"));
                                magDto.setError(getJsonNodeAsDouble(magNode, "error"));
                                magnitudos.add(magDto);
                            }
                        }
                        dto.setMagnitudos(magnitudos);

                        if (node.has("location")) {
                            JsonNode locationNode = node.get("location");
                            LocationDTO locationDto = new LocationDTO();
                            locationDto.setId(getJsonNodeAsInt(locationNode, "id"));
                            locationDto.setLatitude(getJsonNodeAsDouble(locationNode, "latitude"));
                            locationDto.setLongitude(getJsonNodeAsDouble(locationNode, "longitude"));
                            locationDto.setDepth(getJsonNodeAsDouble(locationNode, "depth"));
                            locationDto.setStringLatitude(getJsonNodeAsText(locationNode, "string_latitude"));
                            locationDto.setStringLongitude(getJsonNodeAsText(locationNode, "string_longitude"));
                            locationDto.setQuality(getJsonNodeAsText(locationNode, "quality"));
                            dto.setLocation(locationDto);
                        }

                        return dto;
                    }).collect(Collectors.toList());
                }

                return earthquakes;

            } catch (IOException e) {
                e.printStackTrace();
                return new ArrayList<>();
            }
        } else {
            throw new RuntimeException("Failed : HTTP error code : " + response.getStatusCodeValue());
        }
    }


    private String getJsonNodeAsText(JsonNode node, String fieldName) {
        return node.has(fieldName) ? node.get(fieldName).asText() : null;
    }

    private int getJsonNodeAsInt(JsonNode node, String fieldName) {
        return node.has(fieldName) ? node.get(fieldName).asInt() : 0;
    }

    private double getJsonNodeAsDouble(JsonNode node, String fieldName) {
        return node.has(fieldName) ? node.get(fieldName).asDouble() : 0.0;
    }
}
