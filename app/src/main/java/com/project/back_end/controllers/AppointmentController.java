package com.project.back_end.controllers;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.models.Appointment;
import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.services.*;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Map;
@RestController @RequestMapping("/appointments")
public class AppointmentController {
 private final AppointmentService service;private final AppointmentRepository appointments;private final TokenService tokens;
 public AppointmentController(AppointmentService service,AppointmentRepository appointments,TokenService tokens){this.service=service;this.appointments=appointments;this.tokens=tokens;}
 @PostMapping public Appointment book(@RequestHeader(value="Authorization",required=false) String token,@Valid @RequestBody AppointmentDTO data){return service.bookAppointment(tokens.identity(token,"patient"),data);}
 @GetMapping public Map<String,Object> get(@RequestHeader(value="Authorization",required=false) String token){return Map.of("appointments",appointments.findByDoctorIdOrderByAppointmentTimeAsc(tokens.identity(token,"doctor")));}
 @DeleteMapping("/{id}") public Map<String,String> cancel(@PathVariable Long id,@RequestHeader(value="Authorization",required=false) String token){service.cancel(id,tokens.identity(token,"patient"));return Map.of("message","Appointment cancelled");}
}
