
package com.example.demo.repositories;

import java.util.List;

import com.example.demo.entities.Patient;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    List<Patient> findAll();
    Patient save(Patient doc);
    void delete(Patient doc);
}
