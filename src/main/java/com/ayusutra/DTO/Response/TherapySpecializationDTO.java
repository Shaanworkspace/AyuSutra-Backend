package com.ayusutra.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TherapySpecializationDTO {
    private Long id;
    private String name;
    private String description;
}