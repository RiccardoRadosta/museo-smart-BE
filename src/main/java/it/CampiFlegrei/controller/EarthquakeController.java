package it.CampiFlegrei.controller;

import it.CampiFlegrei.dto.EarthquakeDTO;
import it.CampiFlegrei.service.EarthquakeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/earthquakes")
@CrossOrigin(origins = "http://localhost:4200")
public class EarthquakeController {

    @Autowired
    private EarthquakeService earthquakeService;

    @GetMapping("/updateCache")
    public void updateCache() {
        earthquakeService.getCache();
    }

    @GetMapping("/last24hours/count")
    public int getCountLast24HoursEarthquakes() {
        return earthquakeService.countRecentEarthquakes();
    }

    @GetMapping("/last24hours")
    public List<EarthquakeDTO> getLast24HoursEarthquakes() {
        return earthquakeService.getRecentEarthquakes();
    }

    @GetMapping("/lastweek/counts")
    public Map<String, Integer> getCountsLastWeekEarthquakes() {
        return earthquakeService.countEarthquakesLastWeek() ;
    }

    @GetMapping("/today/count")
    public int getCountTodayEarthquakes() {
        return earthquakeService.countTodayEarthquakes();
    }

    @GetMapping("/last7days/count")
    public int getCountLast7DaysEarthquakes() {
        return earthquakeService.countEarthquakesLast7Days();
    }

    @GetMapping("/last30days/count")
    public int getCountLast30DaysEarthquakes() {
        return earthquakeService.countEarthquakesLast30Days();
    }
}
