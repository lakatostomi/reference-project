package com.example.caloriecalculator.service;

import com.example.caloriecalculator.dto.SportDTO;
import com.example.caloriecalculator.model.Sport;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.repositories.SportRepository;
import com.example.caloriecalculator.repositories.UserRepository;;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class SportService {

    private SportRepository sportRepository;
    private UserRepository userRepository;

    public User saveUsersActivity(SportDTO sportDTO) {
        User user = userRepository.findById(sportDTO.getUser_id()).orElseThrow(()-> new NoSuchElementException("The user is not exist!"));
        user.getSportList().add(convertToSport(sportDTO, user));
        return userRepository.save(user);
    }

    private Sport convertToSport(SportDTO sportDTO, User user) {
        Sport sport = new Sport(
                null,
                sportDTO.getNameOfActivity(),
                user,
                sportDTO.getBurned_calories(),
                new Date()
        );
        return sport;
    }

    public User updateActivity(Integer sport_id, Double burned_calories) {
        Sport sport = sportRepository.getReferenceById(sport_id);
        sport.setBurnedCalories(burned_calories);
        sportRepository.save(sport);
        return userRepository.findById(sport.getUser().getId()).orElseThrow(()-> new NoSuchElementException("The user is not exist!"));
    }

    public Sport findById(Integer id) {
        return sportRepository.findById(id).orElseThrow(()-> new NoSuchElementException("This sport is not exist!"));
    }

    public void deleteActivity(Integer id) {
        sportRepository.deleteById(id);
    }
}
