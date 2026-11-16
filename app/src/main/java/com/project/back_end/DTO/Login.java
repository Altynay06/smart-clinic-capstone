package com.project.back_end.DTO;
import jakarta.validation.constraints.NotBlank;
public record Login(String username,String email,@NotBlank String password){}
