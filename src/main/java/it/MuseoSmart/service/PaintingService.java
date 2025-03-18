package it.MuseoSmart.service;

import it.MuseoSmart.dao.PaintingDAO;
import it.MuseoSmart.dto.PaintingDTO;
import org.springframework.stereotype.Service;

@Service
public class PaintingService {

    private final PaintingDAO paintingDAO;

    public PaintingService(PaintingDAO paintingDAO) {
        this.paintingDAO = paintingDAO;
    }

    public PaintingDTO getPaintingById(Long id) {
        return paintingDAO.getPaintingById(id);
    }
}
