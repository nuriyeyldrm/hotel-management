package com.backendapi.hotelmanagement.service;

import com.backendapi.hotelmanagement.domain.Hotel;
import com.backendapi.hotelmanagement.exception.BadRequestException;
import com.backendapi.hotelmanagement.exception.ResourceNotFoundException;
import com.backendapi.hotelmanagement.repository.HotelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class HotelService {

    private final HotelRepository hotelRepository;
    private static final String HOTEL_NOT_FOUND_MSG = "hotel with id %d not found";

    public List<Hotel> fetchAllHotels() {
        return hotelRepository.findAll();
    }

    public Hotel findById(Long id) throws ResourceNotFoundException {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format(HOTEL_NOT_FOUND_MSG, id)));
    }

    public void createHotel(Hotel hotel) throws BadRequestException {
        try {
            hotelRepository.save(hotel);
        } catch (Exception e) {
            throw new BadRequestException("invalid request");
        }
    }

    public void updateHotel(Hotel hotel) throws BadRequestException {
        try {
            hotelRepository.save(hotel);
        } catch (Exception e) {
            throw new BadRequestException("invalid request");
        }
    }

    public void deleteHotel(Long id) throws ResourceNotFoundException {
        boolean hotelExist = hotelRepository.findById(id).isPresent();

        if (!hotelExist)
            throw new ResourceNotFoundException("hotel not exist");

        hotelRepository.deleteById(id);
    }
}
