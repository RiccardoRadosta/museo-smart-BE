package it.MuseoSmart.dao;

import it.MuseoSmart.dto.PaintingDTO;

public interface PaintingDAO {
    PaintingDTO getPaintingById(Long id);
}
