package com.project.back_end.repo;
import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DoctorRepository extends JpaRepository<Doctor,Long>{java.util.List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyContainingIgnoreCase(String name,String specialty);
 @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
 @org.springframework.data.jpa.repository.Query("select d from Doctor d where d.id=:id")
 java.util.Optional<Doctor> lockById(@org.springframework.data.repository.query.Param("id") Long id);
 java.util.Optional<Doctor> findByEmail(String email);}

