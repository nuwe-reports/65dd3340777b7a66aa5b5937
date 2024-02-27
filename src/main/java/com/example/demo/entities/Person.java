package com.example.demo.entities;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class Person {
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    
    public Person(){
        super();
    }
    
    public Person(String firstName, String lastName, int age, String email){
        super();
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age ;
        this.email = email;
    }

    public String getFirstName(){
        return this.firstName;
    }
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return this.lastName;
    }
    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public int getAge(){
        return this.age;
    }
    public void setAge(int age){
        this.age = age;
    }

    public String getEmail(){
        return this.email;
    }
    public void setEmail(String email){
        this.email = email; 
    }
}
