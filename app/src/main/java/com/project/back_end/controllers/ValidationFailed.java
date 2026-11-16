package com.project.back_end.controllers;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import java.util.Map;
@RestControllerAdvice
public class ValidationFailed {
 @ExceptionHandler(MethodArgumentNotValidException.class) public ResponseEntity<?> invalid(MethodArgumentNotValidException error){return ResponseEntity.badRequest().body(Map.of("message",error.getBindingResult().getFieldErrors().stream().map(e->e.getField()+": "+e.getDefaultMessage()).toList()));}
 @ExceptionHandler(DataIntegrityViolationException.class) public ResponseEntity<?> conflict(){return ResponseEntity.status(409).body(Map.of("message","Record conflicts with existing data"));}
}
