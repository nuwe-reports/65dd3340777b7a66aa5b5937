package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.example.demo.repositories.RoomRepository;
import com.example.demo.entities.Room;


@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
class RoomJpaUnitTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    RoomRepository repository;

    @Test
    void should_find_no_rooms_if_repository_is_empty(){
        Iterable<Room> rooms = repository.findAll();
        assertThat(rooms).isEmpty();
    }

    @Test
    void should_create_a_room(){
        Room room = repository.save(new Room("Dermatology"));

        assertThat(room).hasFieldOrPropertyWithValue("roomName","Dermatology");
    }

    @Test
    void should_find_all_rooms(){

        Room room1 = new Room("Dermatology");
        Room room2 = new Room("Operations");
        Room room3 = new Room("Emergencies");

        entityManager.persist(room1);
        entityManager.persist(room2);
        entityManager.persist(room3);

        Iterable rooms = repository.findAll();

        assertThat(rooms).hasSize(3).contains(room1, room2, room3);
        
    }

    @Test
    void should_find_room_by_id(){
        Room room1 = new Room("Dermatology");
        Room room2 = new Room("Operations");

        entityManager.persist(room1);
        entityManager.persist(room2);

        Optional<Room> foundRoom= repository.findByRoomName(room2.getRoomName());
        Room room = foundRoom.get();

        assertThat(room).isEqualTo(room2);
    }

    @Test
    void should_delete_room(){
        Room room1 = new Room("Dermatology");
        Room room2 = new Room("Operations");
        Room room3 = new Room("Emergencies");

        entityManager.persist(room1);
        entityManager.persist(room2);
        entityManager.persist(room3);

        repository.deleteByRoomName(room2.getRoomName());

        Iterable rooms = repository.findAll();

        assertThat(rooms).hasSize(2).contains(room1, room3);
    }

    @Test
    void should_delete_all_rooms(){
        Room room1 = new Room("Dermatology");
        Room room2 = new Room("Operations");
        Room room3 = new Room("Emergencies");

        entityManager.persist(room1);
        entityManager.persist(room2);
        entityManager.persist(room3);

        repository.deleteAll();
        assertThat(repository.findAll()).isEmpty();
    }
    
}
