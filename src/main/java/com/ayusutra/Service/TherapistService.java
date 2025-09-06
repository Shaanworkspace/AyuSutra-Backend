package com.ayusutra.Service;

import com.ayusutra.DTO.Response.ScheduleSlotDTO;
import com.ayusutra.DTO.Response.TherapistResponseDTO;
import com.ayusutra.DTO.Response.TherapyPlanDTO;
import com.ayusutra.DTO.Response.TherapySpecializationDTO;
import com.ayusutra.Entity.Therapist;
import com.ayusutra.Repository.TherapistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TherapistService {

    private final TherapistRepository therapistRepository;

    // Add new therapist
    public Therapist addTherapist(Therapist therapist) {
        return therapistRepository.save(therapist);
    }

    // Bulk add
    public List<Therapist> createTherapists(List<Therapist> therapists) {
        return therapistRepository.saveAll(therapists);
    }

    // Get all therapists
    public List<TherapistResponseDTO> getAllTherapists() {
        return therapistRepository.findAll().stream()
                .map(this::therapistToDTO)
                .collect(Collectors.toList());
    }

    // Get by Id
    public TherapistResponseDTO getTherapistById(Long id) {
        Therapist therapist = therapistRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("❌ Therapist not found with id " + id));
        return therapistToDTO(therapist);
    }

    // Get by Expertise
    public List<TherapistResponseDTO> getTherapistsByExpertise(String expertise) {
        return therapistRepository.findByExpertise(expertise).stream()
                .map(this::therapistToDTO)
                .collect(Collectors.toList());
    }

    // Login
    public Therapist login(String email, String password) {
        Therapist therapist = therapistRepository.findByEmail(email);
        if (therapist == null) {
            throw new IllegalArgumentException("❌ No Therapist FOUND with email : " + email);
        }
        if (!therapist.getPassword().equals(password)) {
            throw new IllegalArgumentException("❌ Invalid password");
        }
        return therapist;
    }

    // Delete
    public void deleteTherapist(Long id) {
        if (!therapistRepository.existsById(id)) {
            throw new IllegalArgumentException("❌ Therapist not found with id : " + id);
        }
        therapistRepository.deleteById(id);
    }

    //  Helper mapping function
    public TherapistResponseDTO therapistToDTO(Therapist therapist) {
        List<TherapyPlanDTO> planDTOs = therapist.getTherapyPlans().stream()
                .map(plan -> new TherapyPlanDTO(
                        plan.getId(),
                        plan.getTherapyType(),
                        plan.getDurationDays(),
                        plan.getFrequencyPerDay(),
                        plan.getPrecautions(),
                        plan.isApproved(),
                        plan.getPatient() != null ? plan.getPatient().getId() : null,
                        plan.getPatient() != null ? plan.getPatient().getFirstName() + " " + plan.getPatient().getLastName() : null,
                        plan.getDoctor() != null ? plan.getDoctor().getId() : null,
                        plan.getDoctor() != null ? plan.getDoctor().getFirstName() + " " + plan.getDoctor().getLastName() : null
                )).collect(Collectors.toList());

        List<ScheduleSlotDTO> slotDTOs = therapist.getScheduleSlots().stream()
                .map(slot -> new ScheduleSlotDTO(
                        slot.getId(),
                        slot.getDate(),
                        slot.getStartTime(),
                        slot.getEndTime(),
                        slot.getStatus(),
                        slot.getBookedBy() != null ? slot.getBookedBy().getId() : null,
                        slot.getBookedBy() != null ? slot.getBookedBy().getFirstName() + " " + slot.getBookedBy().getLastName() : null
                )).collect(Collectors.toList());

        List<TherapySpecializationDTO> specDTOs = therapist.getSpecializations().stream()
                .map(spec -> new TherapySpecializationDTO(
                        spec.getId(),
                        spec.getName(),
                        spec.getDescription()
                )).collect(Collectors.toList());

        return new TherapistResponseDTO(
                therapist.getId(),
                therapist.getFirstName(),
                therapist.getLastName(),
                therapist.getEmail(),
                therapist.getPhoneNumber(),
                therapist.getQualification(),
                therapist.getYearsOfExperience(),
                therapist.getExpertise(),
                therapist.getLanguagesSpoken(),
                therapist.getClinicLocation(),
                therapist.getBio(),
                planDTOs,
                slotDTOs,
                specDTOs
        );
    }
}