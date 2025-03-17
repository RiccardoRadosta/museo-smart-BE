package it.CampiFlegrei.service;

import it.CampiFlegrei.dao.EarthquakeDAO;
import it.CampiFlegrei.dto.EarthquakeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class EarthquakeService {

    @Autowired
    private EarthquakeDAO earthquakeDAO;

    private List<EarthquakeDTO> cachedEarthquakes;
    private LocalDateTime lastCacheUpdateTime;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public List<EarthquakeDTO> getCache() {
        // Aggiorna la cache
        cachedEarthquakes = earthquakeDAO.getEarthquakeData();
        //lastCacheUpdateTime = LocalDateTime.now(ZoneId.of("Europe/Rome"));
        return cachedEarthquakes;
    }

    private void ensureCacheIsLoaded() {
        if (cachedEarthquakes == null) {
            getCache();
        }
    }

    public List<EarthquakeDTO> getRecentEarthquakes() {
        ensureCacheIsLoaded();

        LocalDateTime now = LocalDateTime.now(ZoneId.of("Europe/Rome"));  // Fuso orario di Roma
        LocalDateTime last24Hours = now.minusHours(24);

        return cachedEarthquakes.stream()
                .filter(e -> {
                    try {
                        LocalDateTime dateTime = LocalDateTime.parse(e.getPrintdate(), formatter);
                        dateTime = dateTime.plusHours(2);  // Incrementa l'ora di 2 ore
                        e.setPrintdate(dateTime.format(formatter));  // Aggiorna la data nel DTO
                        return dateTime.isAfter(last24Hours);
                    } catch (DateTimeParseException ex) {
                        ex.printStackTrace();
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public int countRecentEarthquakes() {
        return getRecentEarthquakes().size();
    }

    private int countEarthquakesOnDay(LocalDate date) {
        ensureCacheIsLoaded();

        return (int) cachedEarthquakes.stream()
                .filter(e -> {
                    try {
                        LocalDateTime dateTime = LocalDateTime.parse(e.getPrintdate(), formatter);
                        dateTime = dateTime.plusHours(2);
                        return dateTime.toLocalDate().isEqual(date);
                    } catch (DateTimeParseException ex) {
                        ex.printStackTrace();
                        return false;
                    }
                })
                .count();
    }

    public Map<String, Integer> countEarthquakesLastWeek() {
        ensureCacheIsLoaded();

        Map<String, Integer> counts = new TreeMap<>((date1, date2) -> {
            LocalDate localDate1 = LocalDate.parse(date1);
            LocalDate localDate2 = LocalDate.parse(date2);
            return localDate1.compareTo(localDate2);
        });

        LocalDate today = LocalDate.now(ZoneId.of("Europe/Rome"));

        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            int count = countEarthquakesOnDay(date);
            counts.put(date.toString(), count);
        }

        return counts;
    }

    public int countTodayEarthquakes() {
        LocalDate today = LocalDate.now(ZoneId.of("Europe/Rome"));
        return countEarthquakesOnDay(today);
    }

    public int countEarthquakesLast7Days() {
        LocalDate today = LocalDate.now(ZoneId.of("Europe/Rome"));
        int total = 0;

        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            total += countEarthquakesOnDay(date);
        }

        return total;
    }

    public int countEarthquakesLast30Days() {
        LocalDate today = LocalDate.now(ZoneId.of("Europe/Rome"));
        int total = 0;

        for (int i = 0; i < 30; i++) {
            LocalDate date = today.minusDays(i);
            total += countEarthquakesOnDay(date);
        }
        return total;
    }
}





