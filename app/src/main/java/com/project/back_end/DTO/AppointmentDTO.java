package com.project.back_end.DTO;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
public record AppointmentDTO(@NotNull Long doctorId,@NotNull @Future LocalDateTime appointmentTime){}
