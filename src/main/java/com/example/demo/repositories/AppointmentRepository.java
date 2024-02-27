
package com.example.demo.repositories;

import java.util.List;

import com.example.demo.entities.Appointment;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAll();
    Appointment save(Appointment appointment);
    void delete(Appointment appointment);
}
