package com.example.demo.controllers;

import com.example.demo.repositories.*;
import com.example.demo.entities.Doctor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class DoctorController {

    @Autowired
    DoctorRepository doctorRepository;

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctor>> getAllDoctors(){
        List<Doctor> doctors = new ArrayList<>();

        doctorRepository.findAll().forEach(doctors::add);

        if (doctors.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(doctors, HttpStatus.OK);
    }

    @GetMapping("/doctors/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable("id") long id){
        Optional<Doctor> doctor = doctorRepository.findById(id);
        if (! doctor.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(doctor.get(),HttpStatus.OK);
    }

    @PostMapping("/doctor")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doc){
        Doctor d = new Doctor(doc.getFirstName(), doc.getLastName(), doc.getAge(), doc.getEmail());
        doctorRepository.save(d);
        return new ResponseEntity<>(d, HttpStatus.CREATED);
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<HttpStatus> deleteDoctor(@PathVariable("id") long id){
        Optional<Doctor> doctor = doctorRepository.findById(id);

        if (! doctor.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        doctorRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/doctors")
    public ResponseEntity<HttpStatus> deleteAllDoctors(){
        doctorRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
