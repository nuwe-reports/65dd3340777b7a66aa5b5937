
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.example.demo.controllers.*;
import com.example.demo.repositories.*;
import com.example.demo.entities.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(DoctorController.class)
class DoctorControllerUnitTest {

    @MockBean
    private DoctorRepository doctorRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Doctor sampleDoctor;

    @BeforeEach
    void setUp() {
        // Set up a sample Doctor for testing
        sampleDoctor = new Doctor("John", "Doe", 35, "john.doe@example.com");
    }

    @Test
    void getAllDoctors_ReturnsListOfDoctors() throws Exception {
        List<Doctor> doctorList = Arrays.asList(sampleDoctor);

        when(doctorRepository.findAll()).thenReturn(doctorList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(doctorList.size()));
    }

    @Test
    void getAllDoctors_EmptyList_ReturnsNoContent() throws Exception {
        // Arrange
        when(doctorRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getDoctorById_ReturnsDoctorById() throws Exception {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(sampleDoctor));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/doctors/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(sampleDoctor.getFirstName()));
    }

    @Test
    void getDoctorById_ReturnsNotFoundForNonExistingDoctor() throws Exception {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/doctors/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createDoctor_CreatesDoctorAndReturnsCreatedStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/doctor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleDoctor)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(sampleDoctor.getFirstName()));
    }

    @Test
    void deleteDoctor_DeletesExistingDoctorAndReturnsOkStatus() throws Exception {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(sampleDoctor));

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/doctors/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deleteDoctor_ReturnsNotFoundForNonExistingDoctor() throws Exception {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/doctors/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteAllDoctors_DeletesAllDoctorsAndReturnsOkStatus() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/doctors")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}

@WebMvcTest(PatientController.class)
class PatientControllerUnitTest {

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllPatients_ReturnsListOfPatients() throws Exception {
        // Arrange
        List<Patient> patients = Arrays.asList(
                new Patient("John", "Doe", 30, "john.doe@example.com"),
                new Patient("Jane", "Smith", 40, "jane.smith@example.com"));
        when(patientRepository.findAll()).thenReturn(patients);

        // Act & Assert
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(patients)));
    }

    @Test
    void getAllPatients_EmptyList_ReturnsNoContent() throws Exception {
        // Arrange
        when(patientRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPatientById_ValidId_ReturnsPatient() throws Exception {
        // Arrange
        long patientId = 1L;
        Patient patient = new Patient("John", "Doe", 30, "john.doe@example.com");
        when(patientRepository.findById(patientId)).thenReturn(Optional.of(patient));

        // Act & Assert
        mockMvc.perform(get("/api/patients/{id}", patientId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(patient)));
    }

    @Test
    void getPatientById_PatientNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        long patientId = 1L;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/patients/{id}", patientId))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPatient_ValidPatient_ReturnsCreatedPatient() throws Exception {
        // Arrange
        Patient patient = new Patient("John", "Doe", 30, "john.doe@example.com");
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        // Act & Assert
        mockMvc.perform(post("/api/patient")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(patient)));
    }

    @Test
    void deletePatient_ValidId_ReturnsOk() throws Exception {
        // Arrange
        long patientId = 1L;
        when(patientRepository.findById(patientId))
                .thenReturn(Optional.of(new Patient("John", "Doe", 30, "john.doe@example.com")));

        // Act & Assert
        mockMvc.perform(delete("/api/patients/{id}", patientId))
                .andExpect(status().isOk());
    }

    @Test
    void deletePatient_PatientNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        long patientId = 1L;
        when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/patients/{id}", patientId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAllPatients_ReturnsOk() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/patients"))
                .andExpect(status().isOk());
    }

}

@WebMvcTest(RoomController.class)
class RoomControllerUnitTest {

    @MockBean
    private RoomRepository roomRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllRooms_ReturnsListOfRooms() throws Exception {
        // Arrange
        List<Room> rooms = Arrays.asList(
                new Room("Room101"),
                new Room("Room102"));
        when(roomRepository.findAll()).thenReturn(rooms);

        // Act & Assert
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(rooms)));
    }

    @Test
    void getAllRooms_EmptyList_ReturnsNoContent() throws Exception {
        // Arrange
        when(roomRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getRoomByRoomName_ValidRoomName_ReturnsRoom() throws Exception {
        // Arrange
        String roomName = "Room101";
        Room room = new Room(roomName);
        when(roomRepository.findByRoomName(roomName)).thenReturn(Optional.of(room));

        // Act & Assert
        mockMvc.perform(get("/api/rooms/{roomName}", roomName))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(room)));
    }

    @Test
    void getRoomByRoomName_RoomNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        String roomName = "NonExistentRoom";
        when(roomRepository.findByRoomName(roomName)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/rooms/{roomName}", roomName))
                .andExpect(status().isNotFound());
    }

    @Test
    void createRoom_ValidRoom_ReturnsCreatedRoom() throws Exception {
        // Arrange
        Room room = new Room("Room103");
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        // Act & Assert
        mockMvc.perform(post("/api/room")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(room)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(room)));
    }

    @Test
    void deleteRoom_ValidRoomName_ReturnsOk() throws Exception {
        // Arrange
        String roomName = "Room101";
        when(roomRepository.findByRoomName(roomName)).thenReturn(Optional.of(new Room(roomName)));

        // Act & Assert
        mockMvc.perform(delete("/api/rooms/{roomName}", roomName))
                .andExpect(status().isOk());
    }

    @Test
    void deleteRoom_RoomNotFound_ReturnsNotFound() throws Exception {
        // Arrange
        String roomName = "NonExistentRoom";
        when(roomRepository.findByRoomName(roomName)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(delete("/api/rooms/{roomName}", roomName))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteAllRooms_ReturnsOk() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/rooms"))
                .andExpect(status().isOk());
    }

}
