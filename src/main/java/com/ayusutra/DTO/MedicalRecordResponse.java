package com.ayusutra.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    // Essentials for doctor & patient
    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private Long therapistId;
    private String therapistName;

    // 👇 New Fields from Entity
    private LocalDate createdDate;
    private String therapyName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private Integer noOfDays;
    private String doctorNotes;
    private Double rating;
}