package com.project.back_end.controllers;
import com.project.back_end.models.*;
import com.project.back_end.repo.*;
import com.project.back_end.services.TokenService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.Map;
@RestController @RequestMapping("/patient")
public class PatientController {
 private final PatientRepository patients;private final AppointmentRepository appointments;private final TokenService tokens;private final BCryptPasswordEncoder passwords;
 public PatientController(PatientRepository patients,AppointmentRepository appointments,TokenService tokens,BCryptPasswordEncoder passwords){this.patients=patients;this.appointments=appointments;this.tokens=tokens;this.passwords=passwords;}
 @PostMapping public Patient register(@Valid @RequestBody Patient patient){if(patients.findByEmail(patient.getEmail()).isPresent())throw new ResponseStatusException(HttpStatus.CONFLICT,"Email already registered");patient.setId(null);patient.setPassword(passwords.encode(patient.getPassword()));return patients.save(patient);}
 @GetMapping("/appointments") public Map<String,Object> appointments(@RequestHeader(value="Authorization",required=false) String token){return Map.of("appointments",appointments.findByPatientIdOrderByAppointmentTimeDesc(tokens.identity(token,"patient")));}
}
