package com.project.back_end.controllers;
import com.project.back_end.models.Prescription;
import com.project.back_end.repo.*;
import com.project.back_end.services.TokenService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import java.util.*;
@RestController @RequestMapping("/prescriptions")
public class PrescriptionController {
 private final PrescriptionRepository prescriptions;private final AppointmentRepository appointments;private final TokenService tokens;
 public PrescriptionController(PrescriptionRepository prescriptions,AppointmentRepository appointments,TokenService tokens){this.prescriptions=prescriptions;this.appointments=appointments;this.tokens=tokens;}
 @PostMapping public Prescription save(@RequestHeader(value="Authorization",required=false) String token,@Valid @RequestBody Prescription data){Long doctor=tokens.identity(token,"doctor");var appointment=appointments.findById(data.appointmentId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));if(!appointment.getDoctor().getId().equals(doctor))throw new ResponseStatusException(HttpStatus.FORBIDDEN);data.id=null;data.doctorId=doctor;data.patientId=appointment.getPatient().getId();return prescriptions.save(data);}
 @GetMapping("/{appointmentId}") public List<Prescription> read(@PathVariable Long appointmentId,@RequestHeader(value="Authorization",required=false) String token){var claims=tokens.validateToken(token,"patient","doctor");Long id=Long.valueOf(claims.getSubject());var appointment=appointments.findById(appointmentId).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));boolean allowed="doctor".equals(claims.get("role"))?appointment.getDoctor().getId().equals(id):appointment.getPatient().getId().equals(id);if(!allowed)throw new ResponseStatusException(HttpStatus.FORBIDDEN);return prescriptions.findByAppointmentId(appointmentId);}
}
