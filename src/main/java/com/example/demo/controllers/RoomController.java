
package com.example.demo.controllers;

import com.example.demo.repositories.*;
import com.example.demo.entities.Room;

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
public class RoomController {

    @Autowired
    RoomRepository roomRepository;

    @GetMapping("/rooms")
    public ResponseEntity<List<Room>> getAllRooms(){
        List<Room> rooms = new ArrayList<>();

        roomRepository.findAll().forEach(rooms::add);

        if (rooms.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/rooms/{roomName}")
    public ResponseEntity<Room> getRoomByRoomName(@PathVariable("roomName") String roomName){
        Optional<Room> room = roomRepository.findByRoomName(roomName);
        if (!room.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(room.get(), HttpStatus.OK);
    }

    @PostMapping("/room")
    public ResponseEntity<Room> createRoom(@RequestBody Room room){
        Room tmp = new Room(room.getRoomName());
        roomRepository.save(tmp);
        return new ResponseEntity<>(tmp, HttpStatus.CREATED);
    }

    @DeleteMapping("/rooms/{roomName}")
    public ResponseEntity<HttpStatus> deleteRoom(@PathVariable("roomName") String roomName){
        Optional<Room> room = roomRepository.findByRoomName(roomName);
        if (! room.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        roomRepository.deleteByRoomName(roomName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/rooms")
    public ResponseEntity<HttpStatus> deleteAllRooms(){
        roomRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
