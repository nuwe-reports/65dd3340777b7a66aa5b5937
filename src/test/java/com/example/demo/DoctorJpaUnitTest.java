package com.example.demo;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.repositories.DoctorRepository;
import com.example.demo.entities.Doctor;


@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
class DoctorJpaUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    DoctorRepository repository;

    @Test
    void should_find_no_doctors_if_repository_is_empty(){
        Iterable<Doctor> doctors = repository.findAll();
        assertThat(doctors).isEmpty();
    }

    @Test
    void should_create_a_doctor(){
        Doctor doc = repository.save(new Doctor("Juan","Carlos", 34, "j.carlos@hospital.accwe"));

        assertThat(doc)
            .hasFieldOrPropertyWithValue("firstName", "Juan")
            .hasFieldOrPropertyWithValue("lastName", "Carlos")
            .hasFieldOrPropertyWithValue("age", 34)
            .hasFieldOrPropertyWithValue("email", "j.carlos@hospital.accwe");
    }

    @Test
    void should_find_all_doctors(){

        Doctor doc1 = new Doctor("Juan","Carlos", 34, "j.carlos@hospital.accwe");
        Doctor doc2 = new Doctor("Cornelio","Andrea", 59, "c.andrea@hospital.accwe");
        Doctor doc3 = new Doctor("Clarisa","Julia", 29, "c.julia@hospital.accwe");

        entityManager.persist(doc1);
        entityManager.persist(doc2);
        entityManager.persist(doc3);

        Iterable doctors = repository.findAll();

        assertThat(doctors).hasSize(3).contains(doc1, doc2, doc3);
        
    }

    @Test
    void should_find_doctor_by_id(){
        Doctor doc1 = new Doctor("Juan","Carlos", 34, "j.carlos@hospital.accwe");
        Doctor doc2 = new Doctor("Cornelio","Andrea", 59, "c.andrea@hospital.accwe");

        entityManager.persist(doc1);
        entityManager.persist(doc2);

        Doctor foundDoc = repository.findById(doc2.getId()).get();

        assertThat(foundDoc).isEqualTo(doc2);
    }

    @Test
    void should_delete_doctor(){
        Doctor doc1 = new Doctor("Juan","Carlos", 34, "j.carlos@hospital.accwe");
        Doctor doc2 = new Doctor("Cornelio","Andrea", 59, "c.andrea@hospital.accwe");
        Doctor doc3 = new Doctor("Clarisa","Julia", 29, "c.julia@hospital.accwe");

        entityManager.persist(doc1);
        entityManager.persist(doc2);
        entityManager.persist(doc3);

        repository.deleteById(doc2.getId());

        Iterable doctors = repository.findAll();

        assertThat(doctors).hasSize(2).contains(doc1, doc3);
    }

    @Test
    void should_delete_all_doctors(){
        Doctor doc1 = new Doctor("Juan","Carlos", 34, "j.carlos@hospital.accwe");
        Doctor doc2 = new Doctor("Cornelio","Andrea", 59, "c.andrea@hospital.accwe");
        Doctor doc3 = new Doctor("Clarisa","Julia", 29, "c.julia@hospital.accwe");

        entityManager.persist(doc1);
        entityManager.persist(doc2);
        entityManager.persist(doc3);

        repository.deleteAll();
        assertThat(repository.findAll()).isEmpty();
    }
    
}
