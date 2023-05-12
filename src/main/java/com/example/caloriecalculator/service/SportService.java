package com.example.caloriecalculator.service;

import com.example.caloriecalculator.dto.SportDTO;
import com.example.caloriecalculator.model.Sport;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.repositories.SportRepository;
import com.example.caloriecalculator.repositories.UserRepository;;
import com.example.caloriecalculator.service.interfaces.ISportService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
@Slf4j
public class SportService implements ISportService {

    private SportRepository sportRepository;
    private UserRepository userRepository;

    @Override
    public User saveUsersActivity(SportDTO sportDTO) {
        log.info("User={} is saving a new sport activity={}", SecurityContextHolder.getContext().getAuthentication().getName(), sportDTO);
        User user = userRepository.findById(sportDTO.getUser_id()).orElseThrow(() -> new NoSuchElementException("This user with id=" + sportDTO.getUser_id() + " is not exist!"));
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

    @Override
    public User updateActivity(Integer sport_id, Double burned_calories) {
        log.info("User={} is updating a sport activity with id={} new burned calories={}", SecurityContextHolder.getContext().getAuthentication().getName(),
        sport_id, burned_calories);
        Sport sport = sportRepository.getReferenceById(sport_id);
        sport.setBurnedCalories(burned_calories);
        sportRepository.save(sport);
        return userRepository.findById(sport.getUser().getId()).orElseThrow(() -> new NoSuchElementException("This user with id=" + sport.getUser().getId() + " is not exist!"));
    }

    @Override
    public Sport findById(Integer id) {
        log.info("User={} is requesting a sport activity with id={}", SecurityContextHolder.getContext().getAuthentication().getName(), id);
        return sportRepository.findById(id).orElseThrow(() -> new NoSuchElementException("This sport is not exist!"));
    }

    @Override
    public void deleteActivity(Integer id) {
        log.info("User={} is deleting a sport activity with id={}", SecurityContextHolder.getContext().getAuthentication().getName(), id);
        sportRepository.deleteById(id);
    }
}
