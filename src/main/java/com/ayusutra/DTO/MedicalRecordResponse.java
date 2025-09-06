package com.ayusutra.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecordResponse {

    private Long id;
    private LocalDateTime visitDate;
    private String symptoms;
    private String diagnosis;
    private String prescribedTreatment;

    // only essentials for doctor & patient
    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private Long therapistId;
    private String therapistName;
}