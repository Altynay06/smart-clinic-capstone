package com.project.back_end.repo;
import com.project.back_end.models.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AppointmentRepository extends JpaRepository<Appointment,Long>{java.util.List<Appointment> findByPatientIdOrderByAppointmentTimeDesc(Long id);
 java.util.List<Appointment> findByDoctorIdOrderByAppointmentTimeAsc(Long id);
 boolean existsByDoctorIdAndStatusAndAppointmentTimeBetween(Long id,int status,java.time.LocalDateTime from,java.time.LocalDateTime to);}

