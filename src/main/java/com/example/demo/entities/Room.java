package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Room {

    @Id
    private String roomName;

    public Room(){
        super();
    }

    public Room( String roomName){
        super();
        this.roomName = roomName;
    }


    public String getRoomName(){
        return this.roomName;
    }

}
