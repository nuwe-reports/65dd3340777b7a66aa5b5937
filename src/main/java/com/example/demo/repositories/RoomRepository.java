
package com.example.demo.repositories;

import java.util.List;
import java.util.Optional;

import com.example.demo.entities.Room;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAll();
    Optional<Room> findByRoomName(String roomName);
    Room save(Room room);
    void delete(Room room);
    void deleteByRoomName(String roomName);
}
