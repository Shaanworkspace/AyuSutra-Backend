package com.ayusutra.Controller;


import com.ayusutra.DTO.Request.LoginRequestDTO;
import com.ayusutra.DTO.Response.ScheduleSlotDTO;
import com.ayusutra.DTO.Response.TherapistResponseDTO;
import com.ayusutra.DTO.Response.WeeklyScheduleDTO;
import com.ayusutra.Entity.ScheduleSlot;
import com.ayusutra.Entity.Therapist;
import com.ayusutra.Service.ScheduleService;
import com.ayusutra.Service.TherapistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/therapists")
@RequiredArgsConstructor
public class TherapistController {

    private final TherapistService therapistService;
    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<TherapistResponseDTO> createTherapist(@RequestBody Therapist therapist) {
        Therapist saved = therapistService.addTherapist(therapist);
        return ResponseEntity.ok(therapistService.therapistToDTO(saved));
    }

    @PostMapping("/bulk")
    public List<Therapist> createTherapists(@RequestBody List<Therapist> therapists) {
        return therapistService.createTherapists(therapists);
    }
    @PutMapping("/{therapistId}/book-slot/{slotId}")
    public ResponseEntity<?> bookSlot(
            @PathVariable Long therapistId,
            @PathVariable Long slotId,
            @RequestParam Long patientId
    ) {
        try {
            ScheduleSlot booked = scheduleService.bookSlot(therapistId, slotId, patientId);

            ScheduleSlotDTO dto = new ScheduleSlotDTO(
                    booked.getId(),
                    booked.getDate(),
                    booked.getStartTime(),
                    booked.getEndTime(),
                    booked.getStatus(),
                    booked.getBookedBy() != null ? booked.getBookedBy().getId() : null,
                    booked.getBookedBy() != null
                            ? booked.getBookedBy().getFirstName() + " " + booked.getBookedBy().getLastName()
                            : null
            );

            return ResponseEntity.ok(dto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("❌ " + e.getMessage());
        }
    }

    @PostMapping("/{therapistId}/schedule-week")
    public ResponseEntity<List<WeeklyScheduleDTO>> generateWeekSchedule(
            @PathVariable Long therapistId,
            @RequestParam String startDate, // Monday date (ISO-8601)
            @RequestBody List<TimeRangeRequest> slotTimes) {

        LocalDate start = LocalDate.parse(startDate);

        // Convert request to a list of time ranges
        List<LocalTime[]> ranges = slotTimes.stream()
                .map(t -> new LocalTime[]{LocalTime.parse(t.getStartTime()), LocalTime.parse(t.getEndTime())})
                .toList();

        List<WeeklyScheduleDTO> weeklySchedule =
                scheduleService.generateWeeklySchedule(therapistId, start, ranges);

        return ResponseEntity.ok(weeklySchedule);
    }
    @GetMapping
    public List<TherapistResponseDTO> getAllTherapists() {
        return therapistService.getAllTherapists();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTherapistById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(therapistService.getTherapistById(id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/expertise/{exp}")
    public List<TherapistResponseDTO> getTherapistsByExpertise(@PathVariable String exp) {
        return therapistService.getTherapistsByExpertise(exp);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        try {
            Therapist therapist = therapistService.login(request.getEmail(), request.getPassword());
            TherapistResponseDTO therapistResponseDTO = therapistService.therapistToDTO(therapist);
            return ResponseEntity.ok(therapistResponseDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTherapist(@PathVariable Long id) {
        try {
            therapistService.deleteTherapist(id);
            return ResponseEntity.ok("✅ Therapist deleted with id"+id);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}