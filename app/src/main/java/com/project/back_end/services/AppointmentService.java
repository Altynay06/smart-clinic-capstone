package com.project.back_end.services;
import com.project.back_end.models.*;
import com.project.back_end.repo.*;
import com.project.back_end.DTO.AppointmentDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.time.*;
import java.time.format.DateTimeFormatter;
@Service
public class AppointmentService {
 private final AppointmentRepository appointments;private final DoctorRepository doctors;private final PatientRepository patients;
 public AppointmentService(AppointmentRepository appointments,DoctorRepository doctors,PatientRepository patients){this.appointments=appointments;this.doctors=doctors;this.patients=patients;}
 @Transactional public Appointment bookAppointment(Long patientId,AppointmentDTO data){
  Doctor doctor=doctors.lockById(data.doctorId()).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Doctor not found"));
  LocalDateTime start=data.appointmentTime();
  if(!start.isAfter(LocalDateTime.now())||start.getMinute()!=0||start.getSecond()!=0)throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Choose a future hourly slot");
  String slot=start.format(DateTimeFormatter.ofPattern("HH:mm"))+"-"+start.plusHours(1).format(DateTimeFormatter.ofPattern("HH:mm"));
  if(!doctor.getAvailableTimes().contains(slot))throw new ResponseStatusException(HttpStatus.CONFLICT,"Doctor is unavailable at this time");
  if(appointments.existsByDoctorIdAndStatusAndAppointmentTimeBetween(doctor.getId(),0,start.minusMinutes(59),start.plusMinutes(59)))throw new ResponseStatusException(HttpStatus.CONFLICT,"Slot already booked");
  Appointment appointment=new Appointment();appointment.setDoctor(doctor);appointment.setPatient(patients.findById(patientId).orElseThrow(()->new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Patient not found")));appointment.setAppointmentTime(start);appointment.setStatus(0);return appointments.save(appointment);
 }
 @Transactional public void cancel(Long id,Long patientId){Appointment a=appointments.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));if(!a.getPatient().getId().equals(patientId))throw new ResponseStatusException(HttpStatus.FORBIDDEN);if(a.getStatus()!=0)throw new ResponseStatusException(HttpStatus.CONFLICT);a.setStatus(2);}
}
