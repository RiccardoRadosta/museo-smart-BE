package it.MuseoSmart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaintingDTO {
    private Long id;
    private String museumName;
    private String paintingName;
    private String authorName;
    private String description;
    private String imgUrl;
}
