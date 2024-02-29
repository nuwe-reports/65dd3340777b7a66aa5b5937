package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.entities.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestInstance(Lifecycle.PER_CLASS)
class EntityUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    private Patient patient;
    private Doctor doctor;
    private Room room;

    @BeforeEach
    void setUp() {
        // Set up sample data for testing
        patient = new Patient("John", "Doe", 30, "john.doe@example.com");
        doctor = new Doctor("Jane", "Smith", 40, "jane.smith@example.com");
        room = new Room("Room101");
    }

    @Test
    void testDoctorEntity() {
        Doctor doctor = new Doctor("John", "Doe", 35, "john.doe@example.com");
        entityManager.persistAndFlush(doctor);

        Doctor retrievedDoctor = entityManager.find(Doctor.class, doctor.getId());

        assertThat(retrievedDoctor).isEqualTo(doctor);
    }

    @Test
    void testPatientEntity() {
        Patient patient = new Patient("Alice", "Smith", 25, "alice.smith@example.com");
        entityManager.persistAndFlush(patient);

        Patient retrievedPatient = entityManager.find(Patient.class, patient.getId());

        assertThat(retrievedPatient).isEqualTo(patient);
    }

    @Test
    void testRoomEntity() {
        Room room = new Room("Room101");
        entityManager.persistAndFlush(room);

        Room retrievedRoom = entityManager.find(Room.class, room.getRoomName());

        assertThat(retrievedRoom).isEqualTo(room);
    }

    @Test
    void testAppointmentEntity() {
        Doctor doctor = new Doctor("John", "Doe", 35, "john.doe@example.com");
        entityManager.persistAndFlush(doctor);

        Patient patient = new Patient("Alice", "Smith", 25, "alice.smith@example.com");
        entityManager.persistAndFlush(patient);

        Room room = new Room("Room101");
        entityManager.persistAndFlush(room);

        LocalDateTime startsAt = LocalDateTime.now();
        LocalDateTime finishesAt = startsAt.plusHours(1);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);
        entityManager.persistAndFlush(appointment);

        Appointment retrievedAppointment = entityManager.find(Appointment.class, appointment.getId());

        assertThat(retrievedAppointment).isEqualTo(appointment);
    }

    @Test
    @DisplayName("Test Appointments Start and Finish at the Same Time")
    void testAppointmentsStartAndFinishAtTheSameTime() {
        LocalDateTime startsAt1 = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime finishesAt1 = LocalDateTime.of(2022, 1, 1, 11, 0);

        LocalDateTime startsAt2 = startsAt1;
        LocalDateTime finishesAt2 = finishesAt1;

        Appointment appointment1 = new Appointment(patient, doctor, room, startsAt1, finishesAt1);
        Appointment appointment2 = new Appointment(patient, doctor, room, startsAt2, finishesAt2);

        assertTrue(appointment1.overlaps(appointment2));
    }

    @Test
    @DisplayName("Test Appointments Finish at the Same Time")
    void testAppointmentsFinishAtTheSameTime() {
        LocalDateTime startsAt1 = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime finishesAt1 = LocalDateTime.of(2022, 1, 1, 11, 0);

        LocalDateTime startsAt2 = LocalDateTime.of(2022, 1, 1, 11, 0);
        LocalDateTime finishesAt2 = finishesAt1;

        Appointment appointment1 = new Appointment(patient, doctor, room, startsAt1, finishesAt1);
        Appointment appointment2 = new Appointment(patient, doctor, room, startsAt2, finishesAt2);

        assertTrue(appointment1.overlaps(appointment2));
    }

    @Test
    @DisplayName("Test Appointments Start at the Same Time")
    void testAppointmentsStartAtTheSameTime() {
        LocalDateTime startsAt1 = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime finishesAt1 = LocalDateTime.of(2022, 1, 1, 11, 0);

        LocalDateTime startsAt2 = startsAt1;
        LocalDateTime finishesAt2 = LocalDateTime.of(2022, 1, 1, 10, 30);

        Appointment appointment1 = new Appointment(patient, doctor, room, startsAt1, finishesAt1);
        Appointment appointment2 = new Appointment(patient, doctor, room, startsAt2, finishesAt2);

        assertTrue(appointment1.overlaps(appointment2));
    }

    @Test
    @DisplayName("Test Appointments Overlap")
    void testAppointmentsOverlap() {
        LocalDateTime startsAt1 = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime finishesAt1 = LocalDateTime.of(2022, 1, 1, 11, 0);

        LocalDateTime startsAt2 = LocalDateTime.of(2022, 1, 1, 10, 30);
        LocalDateTime finishesAt2 = LocalDateTime.of(2022, 1, 1, 11, 30);

        Appointment appointment1 = new Appointment(patient, doctor, room, startsAt1, finishesAt1);
        Appointment appointment2 = new Appointment(patient, doctor, room, startsAt2, finishesAt2);

        assertTrue(appointment1.overlaps(appointment2));
    }

    @Test
    @DisplayName("Test Appointments Do Not Overlap")
    void testAppointmentsDoNotOverlap() {
        LocalDateTime startsAt1 = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime finishesAt1 = LocalDateTime.of(2022, 1, 1, 11, 0);

        LocalDateTime startsAt2 = LocalDateTime.of(2022, 1, 1, 12, 0);
        LocalDateTime finishesAt2 = LocalDateTime.of(2022, 1, 1, 13, 0);

        Appointment appointment1 = new Appointment(patient, doctor, room, startsAt1, finishesAt1);
        Appointment appointment2 = new Appointment(patient, doctor, room, startsAt2, finishesAt2);

        assertFalse(appointment1.overlaps(appointment2));
    }

    @Test
    @DisplayName("Test Appointments Finish Within the Time Frame")
    void testAppointmentsFinishWithinTheTimeFrame() {
        LocalDateTime startsAt1 = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime finishesAt1 = LocalDateTime.of(2022, 1, 1, 11, 0);

        LocalDateTime startsAt2 = LocalDateTime.of(2022, 1, 1, 10, 30);
        LocalDateTime finishesAt2 = LocalDateTime.of(2022, 1, 1, 10, 45);

        Appointment appointment1 = new Appointment(patient, doctor, room, startsAt1, finishesAt1);
        Appointment appointment2 = new Appointment(patient, doctor, room, startsAt2, finishesAt2);

        assertTrue(appointment1.overlaps(appointment2));
    }

    @Test
    @DisplayName("Test Appointments Start Within the Time Frame")
    void testAppointmentsStartWithinTheTimeFrame() {
        LocalDateTime startsAt1 = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime finishesAt1 = LocalDateTime.of(2022, 1, 1, 11, 0);

        LocalDateTime startsAt2 = LocalDateTime.of(2022, 1, 1, 10, 30);
        LocalDateTime finishesAt2 = LocalDateTime.of(2022, 1, 1, 11, 30);

        Appointment appointment1 = new Appointment(patient, doctor, room, startsAt1, finishesAt1);
        Appointment appointment2 = new Appointment(patient, doctor, room, startsAt2, finishesAt2);

        assertTrue(appointment1.overlaps(appointment2));
    }

    @Test
    @DisplayName("Test Appointments Outside the Time Frame")
    void testAppointmentsOutsideTheTimeFrame() {
        LocalDateTime startsAt1 = LocalDateTime.of(2022, 1, 1, 10, 0);
        LocalDateTime finishesAt1 = LocalDateTime.of(2022, 1, 1, 11, 0);

        LocalDateTime startsAt2 = LocalDateTime.of(2022, 1, 1, 12, 0);
        LocalDateTime finishesAt2 = LocalDateTime.of(2022, 1, 1, 13, 0);

        Appointment appointment1 = new Appointment(patient, doctor, room, startsAt1, finishesAt1);
        Appointment appointment2 = new Appointment(patient, doctor, room, startsAt2, finishesAt2);

        assertFalse(appointment1.overlaps(appointment2));
    }
}