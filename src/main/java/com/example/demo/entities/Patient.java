package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Patient extends Person{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    public Patient(){
        super();
    }

    public Patient(String firstName, String lastName, int age, String email){
        super(firstName, lastName, age, email);
    }

    public long getId(){
        return this.id;
    }
    public void setId(long id){
        this.id = id;
    }

}
