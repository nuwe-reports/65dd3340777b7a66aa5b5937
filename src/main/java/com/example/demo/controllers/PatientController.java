
package com.example.demo.controllers;

import com.example.demo.repositories.*;
import com.example.demo.entities.Patient;

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
public class PatientController {

    @Autowired
    PatientRepository patientRepository;

    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients(){
        List<Patient> patients = new ArrayList<>();

        patientRepository.findAll().forEach(patients::add);

        if (patients.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(patients, HttpStatus.OK);
    }

    @GetMapping("/patients/{id}")
    public ResponseEntity<Patient> getPatientById(@PathVariable("id") long id){
        Optional<Patient> patient = patientRepository.findById(id);
        if (! patient.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(patient.get(),HttpStatus.OK);
    }

    @PostMapping("/patient")
    public ResponseEntity<Patient> createPatient(@RequestBody Patient pat){
        Patient d = new Patient(pat.getFirstName(), pat.getLastName(), pat.getAge(), pat.getEmail());
        patientRepository.save(d);
        return new ResponseEntity<>(d, HttpStatus.CREATED);
    }

    @DeleteMapping("/patients/{id}")
    public ResponseEntity<HttpStatus> deletePatient(@PathVariable("id") long id){
        Optional<Patient> patient = patientRepository.findById(id);
        if (! patient.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        patientRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/patients")
    public ResponseEntity<HttpStatus> deleteAllPatients(){
        patientRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
