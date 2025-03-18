package it.MuseoSmart.controller;

import it.MuseoSmart.dto.PaintingDTO;
import it.MuseoSmart.service.PaintingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paintings")
public class PaintingController {

    private final PaintingService paintingService;

    @Autowired
    public PaintingController(PaintingService paintingService) {
        this.paintingService = paintingService;
    }

    @GetMapping("/{id}")
    public PaintingDTO getPaintingById(@PathVariable Long id) {
        return paintingService.getPaintingById(id);
    }
}
