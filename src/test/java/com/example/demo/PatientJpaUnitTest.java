package com.example.demo;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.repositories.PatientRepository;
import com.example.demo.entities.Patient;


@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
class PatientJpaUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    PatientRepository repository;

    @Test
    void should_find_no_patients_if_repository_is_empty(){
        Iterable<Patient> patients = repository.findAll();
        assertThat(patients).isEmpty();
    }

    @Test
    void should_create_a_patient(){
        Patient patient = repository.save(new Patient("Juan","Carlos", 34, "j.carlos@hospital.accwe"));

        assertThat(patient)
            .hasFieldOrPropertyWithValue("firstName", "Juan")
            .hasFieldOrPropertyWithValue("lastName", "Carlos")
            .hasFieldOrPropertyWithValue("age", 34) 
            .hasFieldOrPropertyWithValue("email", "j.carlos@hospital.accwe");
    }

    @Test
    void should_find_all_patients(){

        Patient patient1 = new Patient("Juan","Carlos", 34, "j.carlos@hospital.accwe");
        Patient patient2 = new Patient("Cornelio","Andrea", 59, "c.andrea@hospital.accwe");
        Patient patient3 = new Patient("Clarisa","Julia", 29, "c.julia@hospital.accwe");

        entityManager.persist(patient1);
        entityManager.persist(patient2);
        entityManager.persist(patient3);

        Iterable patients = repository.findAll();

        assertThat(patients).hasSize(3).contains(patient1, patient2, patient3);
        
    }

    @Test
    void should_find_patient_by_id(){
        Patient patient1 = new Patient("Juan","Carlos", 34, "j.carlos@hospital.accwe");
        Patient patient2 = new Patient("Cornelio","Andrea", 59, "c.andrea@hospital.accwe");

        entityManager.persist(patient1);
        entityManager.persist(patient2);

        Patient foundPatient = repository.findById(patient2.getId()).get();

        assertThat(foundPatient).isEqualTo(patient2);
    }

    @Test
    void should_delete_patient(){
        Patient patient1 = new Patient("Juan","Carlos", 34, "j.carlos@hospital.accwe");
        Patient patient2 = new Patient("Cornelio","Andrea", 59, "c.andrea@hospital.accwe");
        Patient patient3 = new Patient("Clarisa","Julia", 29, "c.julia@hospital.accwe");

        entityManager.persist(patient1);
        entityManager.persist(patient2);
        entityManager.persist(patient3);

        repository.deleteById(patient2.getId());

        Iterable patients = repository.findAll();

        assertThat(patients).hasSize(2).contains(patient1, patient3);
    }

    @Test
    void should_delete_all_patients(){
        Patient patient1 = new Patient("Juan","Carlos", 34, "j.carlos@hospital.accwe");
        Patient patient2 = new Patient("Cornelio","Andrea", 59, "c.andrea@hospital.accwe");
        Patient patient3 = new Patient("Clarisa","Julia", 29, "c.julia@hospital.accwe");

        entityManager.persist(patient1);
        entityManager.persist(patient2);
        entityManager.persist(patient3);

        repository.deleteAll();
        assertThat(repository.findAll()).isEmpty();
    }
    
}
