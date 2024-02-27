package com.example.demo.repositories;

import java.util.List;

import com.example.demo.entities.Doctor;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    List<Doctor> findAll();
    Doctor save(Doctor doc);
    void delete(Doctor doc);
}
