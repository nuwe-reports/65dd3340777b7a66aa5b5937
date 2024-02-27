package com.example.demo;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.repositories.*;
import com.example.demo.entities.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
class AppointmentJpaUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    AppointmentRepository repoAppointments;
    
    @Autowired
    DoctorRepository repoDoctors;

    @Autowired
    PatientRepository repoPatients;

    @Autowired
    RoomRepository repoRooms;

    @Test
    void should_find_no_appointments_if_repository_is_empty(){
        Iterable<Appointment> appointments = repoAppointments.findAll();
        assertThat(appointments).isEmpty();
    }

    @Test
    void should_create_a_appointment(){
        // Appointment (Patient, Doctor, Room, startsAt, finishesAt)
        Patient patient = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Doctor doctor = new Doctor ("Perla", "Amalia", 24, "p.amalia@hospital.accwe");
        Room room = new Room("Dermatology");

        LocalDateTime startsAt = LocalDateTime.now();
        LocalDateTime finishesAt = LocalDateTime.now();

        entityManager.persist(patient);
        entityManager.persist(doctor);
        entityManager.persist(room);

        Appointment appointment = new Appointment(patient, doctor, room, startsAt, finishesAt);

        assertThat(appointment)
            .hasFieldOrPropertyWithValue("patient", patient)
            .hasFieldOrPropertyWithValue("doctor", doctor)
            .hasFieldOrPropertyWithValue("room", room)
            .hasFieldOrPropertyWithValue("startsAt", startsAt)
            .hasFieldOrPropertyWithValue("finishesAt", finishesAt);
    }

    @Test
    void should_find_all_appointments(){
        // Appointment (Patient, Doctor, Room, startsAt, finishesAt)
        Patient patient1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Patient patient2 = new Patient("Mariela", "Eric", 42, "m.eric@email.com");
        Patient patient3 = new Patient("Balduino", "Yamile", 67, "b.yamile@email.com");

        Doctor doctor1 = new Doctor ("Carina", "Zaray", 49, "c.zaray@hospital.accwe");
        Doctor doctor2 = new Doctor ("Reyna", "Cayetana", 28, "r.cayetana@hospital.accwe");
        Doctor doctor3 = new Doctor ("Fidela", "Filemon", 24, "f.filemon@hospital.accwe");

        Room room1 = new Room("Dermatology");
        Room room2 = new Room("Oncology");
        Room room3 = new Room("Emergency");

        LocalDateTime startsAt1 = LocalDateTime.now();
        LocalDateTime startsAt2 = LocalDateTime.now();
        LocalDateTime startsAt3 = LocalDateTime.now();

        LocalDateTime finishesAt1 = LocalDateTime.now();
        LocalDateTime finishesAt2 = LocalDateTime.now();
        LocalDateTime finishesAt3 = LocalDateTime.now();

        entityManager.persist(patient1);
        entityManager.persist(patient2);
        entityManager.persist(patient3);

        entityManager.persist(doctor1);
        entityManager.persist(doctor2);
        entityManager.persist(doctor3);

        entityManager.persist(room1);
        entityManager.persist(room2);
        entityManager.persist(room3);
        
        Appointment appointment1 = new Appointment(patient1, doctor1, room1, startsAt1, finishesAt1);
        Appointment appointment2 = new Appointment(patient2, doctor2, room2, startsAt2, finishesAt2);
        Appointment appointment3 = new Appointment(patient3, doctor3, room3, startsAt3, finishesAt3);

        entityManager.persist(appointment1);
        entityManager.persist(appointment2);
        entityManager.persist(appointment3);

        Iterable appointments = repoAppointments.findAll();

        assertThat(appointments).hasSize(3).contains(appointment1, appointment2, appointment3);
        
    }

    @Test
    void should_find_appointment_by_id(){
        // Appointment (Patient, Doctor, Room, startsAt, finishesAt)
        Patient patient1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Patient patient2 = new Patient("Mariela", "Eric", 42, "m.eric@email.com");
        Patient patient3 = new Patient("Balduino", "Yamile", 67, "b.yamile@email.com");

        Doctor doctor1 = new Doctor ("Carina", "Zaray", 49, "c.zaray@hospital.accwe");
        Doctor doctor2 = new Doctor ("Reyna", "Cayetana", 28, "r.cayetana@hospital.accwe");
        Doctor doctor3 = new Doctor ("Fidela", "Filemon", 24, "f.filemon@hospital.accwe");

        Room room1 = new Room("Dermatology");
        Room room2 = new Room("Oncology");
        Room room3 = new Room("Emergency");

        LocalDateTime startsAt1 = LocalDateTime.now();
        LocalDateTime startsAt2 = LocalDateTime.now();
        LocalDateTime startsAt3 = LocalDateTime.now();

        LocalDateTime finishesAt1 = LocalDateTime.now();
        LocalDateTime finishesAt2 = LocalDateTime.now();
        LocalDateTime finishesAt3 = LocalDateTime.now();

        entityManager.persist(patient1);
        entityManager.persist(patient2);
        entityManager.persist(patient3);

        entityManager.persist(doctor1);
        entityManager.persist(doctor2);
        entityManager.persist(doctor3);

        entityManager.persist(room1);
        entityManager.persist(room2);
        entityManager.persist(room3);
        
        Appointment appointment1 = new Appointment(patient1, doctor1, room1, startsAt1, finishesAt1);
        Appointment appointment2 = new Appointment(patient2, doctor2, room2, startsAt2, finishesAt2);
        Appointment appointment3 = new Appointment(patient3, doctor3, room3, startsAt3, finishesAt3);

        entityManager.persist(appointment1);
        entityManager.persist(appointment2);
        entityManager.persist(appointment3);

        Appointment foundAppointment = repoAppointments.findById(appointment2.getId()).get(); 
        assertThat(foundAppointment).isEqualTo(appointment2);
    }

    @Test
    void should_delete_appointment(){
        // Appointment (Patient, Doctor, Room, startsAt, finishesAt)
        Patient patient1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Patient patient2 = new Patient("Mariela", "Eric", 42, "m.eric@email.com");
        Patient patient3 = new Patient("Balduino", "Yamile", 67, "b.yamile@email.com");

        Doctor doctor1 = new Doctor ("Carina", "Zaray", 49, "c.zaray@hospital.accwe");
        Doctor doctor2 = new Doctor ("Reyna", "Cayetana", 28, "r.cayetana@hospital.accwe");
        Doctor doctor3 = new Doctor ("Fidela", "Filemon", 24, "f.filemon@hospital.accwe");

        Room room1 = new Room("Dermatology");
        Room room2 = new Room("Oncology");
        Room room3 = new Room("Emergency");

        LocalDateTime startsAt1 = LocalDateTime.now();
        LocalDateTime startsAt2 = LocalDateTime.now();
        LocalDateTime startsAt3 = LocalDateTime.now();

        LocalDateTime finishesAt1 = LocalDateTime.now();
        LocalDateTime finishesAt2 = LocalDateTime.now();
        LocalDateTime finishesAt3 = LocalDateTime.now();

        entityManager.persist(patient1);
        entityManager.persist(patient2);
        entityManager.persist(patient3);

        entityManager.persist(doctor1);
        entityManager.persist(doctor2);
        entityManager.persist(doctor3);

        entityManager.persist(room1);
        entityManager.persist(room2);
        entityManager.persist(room3);
        
        Appointment appointment1 = new Appointment(patient1, doctor1, room1, startsAt1, finishesAt1);
        Appointment appointment2 = new Appointment(patient2, doctor2, room2, startsAt2, finishesAt2);
        Appointment appointment3 = new Appointment(patient3, doctor3, room3, startsAt3, finishesAt3);

        entityManager.persist(appointment1);
        entityManager.persist(appointment2);
        entityManager.persist(appointment3);

        repoAppointments.deleteById(appointment2.getId());

        Iterable appointments = repoAppointments.findAll();
        assertThat(appointments).hasSize(2).contains(appointment1, appointment3);
    }

    @Test
    void should_delete_all_appointments(){
        // Appointment (Patient, Doctor, Room, startsAt, finishesAt)
        Patient patient1 = new Patient("Jose Luis", "Olaya", 37, "j.olaya@email.com");
        Patient patient2 = new Patient("Mariela", "Eric", 42, "m.eric@email.com");
        Patient patient3 = new Patient("Balduino", "Yamile", 67, "b.yamile@email.com");

        Doctor doctor1 = new Doctor ("Carina", "Zaray", 49, "c.zaray@hospital.accwe");
        Doctor doctor2 = new Doctor ("Reyna", "Cayetana", 28, "r.cayetana@hospital.accwe");
        Doctor doctor3 = new Doctor ("Fidela", "Filemon", 24, "f.filemon@hospital.accwe");

        Room room1 = new Room("Dermatology");
        Room room2 = new Room("Oncology");
        Room room3 = new Room("Emergency");

        LocalDateTime startsAt1 = LocalDateTime.now();
        LocalDateTime startsAt2 = LocalDateTime.now();
        LocalDateTime startsAt3 = LocalDateTime.now();

        LocalDateTime finishesAt1 = LocalDateTime.now();
        LocalDateTime finishesAt2 = LocalDateTime.now();
        LocalDateTime finishesAt3 = LocalDateTime.now();

        entityManager.persist(patient1);
        entityManager.persist(patient2);
        entityManager.persist(patient3);

        entityManager.persist(doctor1);
        entityManager.persist(doctor2);
        entityManager.persist(doctor3);

        entityManager.persist(room1);
        entityManager.persist(room2);
        entityManager.persist(room3);
        
        Appointment appointment1 = new Appointment(patient1, doctor1, room1, startsAt1, finishesAt1);
        Appointment appointment2 = new Appointment(patient2, doctor2, room2, startsAt2, finishesAt2);
        Appointment appointment3 = new Appointment(patient3, doctor3, room3, startsAt3, finishesAt3);

        entityManager.persist(appointment1);
        entityManager.persist(appointment2);
        entityManager.persist(appointment3);

        repoAppointments.deleteAll(); 
        Iterable appointments = repoAppointments.findAll();
        assertThat(appointments).isEmpty();
    }
    
}
