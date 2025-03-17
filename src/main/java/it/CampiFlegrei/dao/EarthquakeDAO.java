package it.CampiFlegrei.dao;

import it.CampiFlegrei.dto.EarthquakeDTO;
import java.util.List;

public interface EarthquakeDAO {
    List<EarthquakeDTO> getEarthquakeData();
}
