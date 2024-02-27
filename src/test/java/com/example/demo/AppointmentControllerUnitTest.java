package com.example.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;
import java.time.format.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.demo.controllers.AppointmentController;
import com.example.demo.repositories.*;
import com.example.demo.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerUnitTest{

    @MockBean
    private AppointmentRepository appointmentRepository;

    @Autowired 
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAppointment() throws Exception {
        
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Room room = new Room("Dermatology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        
        LocalDateTime startsAt= LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);

        mockMvc.perform(post("/api/appointment").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isOk());
                
    }

    @Test
    void shouldNotCreateAppointment() throws Exception {
        
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Room room = new Room("Dermatology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        
        LocalDateTime startsAt= LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("19:30 24/04/2023", formatter);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);

        mockMvc.perform(post("/api/appointment").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldCreateOneAppointmentOutOfTwoConflictDate() throws Exception{
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Patient patient2 = new Patient("Paulino", "Antunez", 37, "p.antunez@email.com");
        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Doctor doctor2 = new Doctor ("Miren", "Iniesta", 24, "m.iniesta@hospital.accwe");
        Room room = new Room("Dermatology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        
        LocalDateTime startsAt= LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);
        Appointment appointment2 = new Appointment(patient2, doctor2, room, startsAt, finishesAt);
        

        mockMvc.perform(post("/api/appointment").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isOk());
                



        List<Appointment> appointments = new ArrayList<Appointment>();
        appointments.add(appointment);
        
        when(appointmentRepository.findAll()).thenReturn(appointments);
        mockMvc.perform(post("/api/appointment").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment2)))
                .andExpect(status().isNotAcceptable());
                

    }

    @Test
    void shouldCreateBothAppointmentsConflictDateButNotRoom() throws Exception {

        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Patient patient2 = new Patient("Paulino", "Antunez", 37, "p.antunez@email.com");
        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Doctor doctor2 = new Doctor ("Miren", "Iniesta", 24, "m.iniesta@hospital.accwe");
        Room room = new Room("Dermatology");
        Room room2 = new Room("Oncology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        
        LocalDateTime startsAt= LocalDateTime.parse("19:30 24/04/2023", formatter);
        LocalDateTime finishesAt = LocalDateTime.parse("20:30 24/04/2023", formatter);

        doctor2.setId(2);
        patient2.setId(2);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);
        Appointment appointment2 = new Appointment(patient2, doctor2, room2, startsAt, finishesAt);

        mockMvc.perform(post("/api/appointment").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment)))
                .andExpect(status().isOk());
                



        List<Appointment> appointments = new ArrayList<Appointment>();
        appointments.add(appointment);
        
        when(appointmentRepository.findAll()).thenReturn(appointments);
        mockMvc.perform(post("/api/appointment").contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointment2)))
                .andExpect(status().isOk());
                

    }
    
    @Test
    void shouldGetNoAppointments() throws Exception{
        List<Appointment> appointments = new ArrayList<Appointment>();
        when(appointmentRepository.findAll()).thenReturn(appointments);
        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isNoContent());
                
    }

    @Test
    void shouldGetTwoAppointments() throws Exception{
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Patient patient2 = new Patient("Paulino", "Antunez", 37, "p.antunez@email.com");
        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Doctor doctor2 = new Doctor ("Miren", "Iniesta", 24, "m.iniesta@hospital.accwe");
        Room room = new Room("Dermatology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        
        LocalDateTime startsAt= LocalDateTime.parse("19:00 24/04/2023", formatter);
        LocalDateTime startsAt2= LocalDateTime.parse("19:30 24/04/2023", formatter);

        LocalDateTime finishesAt = LocalDateTime.parse("20:00 24/04/2023", formatter);
        LocalDateTime finishesAt2 = LocalDateTime.parse("20:30 24/04/2023", formatter);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);
        Appointment appointment2 = new Appointment(patient2, doctor2, room, startsAt2, finishesAt2);

        List<Appointment> appointments = new ArrayList<Appointment>();
        appointments.add(appointment);
        appointments.add(appointment2);

        when(appointmentRepository.findAll()).thenReturn(appointments);
        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk());
                
    }

    @Test
    void shouldGetAppointmentById() throws Exception{
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Room room = new Room("Dermatology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        
        LocalDateTime startsAt= LocalDateTime.parse("19:00 24/04/2023", formatter);

        LocalDateTime finishesAt = LocalDateTime.parse("20:00 24/04/2023", formatter);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);

        appointment.setId(1);

        Optional<Appointment> opt = Optional.of(appointment);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(appointment.getId());
        assertThat(appointment.getId()).isEqualTo(1);

        when(appointmentRepository.findById(appointment.getId())).thenReturn(opt);
        mockMvc.perform(get("/api/appointments/" + appointment.getId()))
                .andExpect(status().isOk());
                
    }
    
    @Test
    void shouldNotGetAnyAppointmentById() throws Exception{
        long id = 31;
        mockMvc.perform(get("/api/appointments/" + id))
                .andExpect(status().isNotFound());
                
    }

    @Test
    void shouldDeleteAppointmentById() throws Exception{
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Room room = new Room("Dermatology");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
        
        LocalDateTime startsAt= LocalDateTime.parse("19:00 24/04/2023", formatter);

        LocalDateTime finishesAt = LocalDateTime.parse("20:00 24/04/2023", formatter);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);

        appointment.setId(1);

        Optional<Appointment> opt = Optional.of(appointment);

        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(appointment.getId());
        assertThat(appointment.getId()).isEqualTo(1);

        when(appointmentRepository.findById(appointment.getId())).thenReturn(opt);
        mockMvc.perform(delete("/api/appointments/" + appointment.getId()))
                .andExpect(status().isOk());
                
    }

    @Test
    void shouldNotDeleteAppointment() throws Exception{
        long id = 31;
        mockMvc.perform(delete("/api/appointments/" + id))
                .andExpect(status().isNotFound());
                
    }

    @Test
    void shouldDeleteAllAppointments() throws Exception{
        mockMvc.perform(delete("/api/appointments"))
                .andExpect(status().isOk());
                
    }
}

