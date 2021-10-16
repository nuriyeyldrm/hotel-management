package com.backendapi.hotelmanagement.service;

import com.backendapi.hotelmanagement.domain.Room;
import com.backendapi.hotelmanagement.exception.BadRequestException;
import com.backendapi.hotelmanagement.exception.ResourceNotFoundException;
import com.backendapi.hotelmanagement.repository.RoomRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;
    private static final String ROOM_NOT_FOUND_MSG = "room with id %d not found";

    public List<Room> fetchAllRooms() {
        return roomRepository.findAll();
    }

    public Room findById(Long id) throws ResourceNotFoundException {
        return roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(ROOM_NOT_FOUND_MSG, id)));
    }

    public void createRoom(Room room) throws BadRequestException {
        try {
            roomRepository.save(room);
        } catch (Exception e) {
            throw new BadRequestException("invalid request");
        }
    }

    public void updateRoom(Room room) throws BadRequestException {
        try {
            roomRepository.save(room);
        } catch (Exception e) {
            throw new BadRequestException("invalid request");
        }
    }

    public void deleteRoom(Long id) throws ResourceNotFoundException {
        boolean roomExist = roomRepository.findById(id).isPresent();

        if (!roomExist)
            throw new ResourceNotFoundException("room does not exist");

        roomRepository.deleteById(id);
    }
}
