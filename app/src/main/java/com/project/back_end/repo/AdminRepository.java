package com.project.back_end.repo;
import com.project.back_end.models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AdminRepository extends JpaRepository<Admin,Long>{java.util.Optional<Admin> findByUsername(String username);}

