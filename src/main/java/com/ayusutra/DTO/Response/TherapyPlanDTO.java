package com.ayusutra.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TherapyPlanDTO {
    private Long id;
    private String therapyType;
    private int durationDays;
    private int frequencyPerDay;
    private String precautions;
    private boolean approved;

    private Long patientId;
    private String patientName;
    private Long doctorId;
    private String doctorName;
}