package com.project.back_end.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.*;
import java.util.*;

@Entity
@Table(name="appointment")
public class Appointment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
 private Long id;
 @ManyToOne(optional=false) @JoinColumn(name="doctor_id") @NotNull
 private Doctor doctor;
 @ManyToOne(optional=false) @JoinColumn(name="patient_id") @NotNull
 private Patient patient;
 @NotNull @Future
 private LocalDateTime appointmentTime;
 @Min(0) @Max(2)
 private int status;
 public Long getId(){return id;}
 public void setId(Long value){this.id=value;}
 public Doctor getDoctor(){return doctor;}
 public void setDoctor(Doctor value){this.doctor=value;}
 public Patient getPatient(){return patient;}
 public void setPatient(Patient value){this.patient=value;}
 public LocalDateTime getAppointmentTime(){return appointmentTime;}
 public void setAppointmentTime(LocalDateTime value){this.appointmentTime=value;}
 public int getStatus(){return status;}
 public void setStatus(int value){this.status=value;}
 @Transient public LocalDateTime getEndTime(){return appointmentTime.plusHours(1);}
 @Transient public LocalDate getAppointmentDate(){return appointmentTime.toLocalDate();}
 @Transient public LocalTime getAppointmentTimeOnly(){return appointmentTime.toLocalTime();}
}
