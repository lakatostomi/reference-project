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
    public User saveUsersSport(SportDTO sportDTO) {
        log.info("User={} is saving a new sport activity={}", SecurityContextHolder.getContext().getAuthentication().getName(), sportDTO);
        User user = getUser(sportDTO.getUser_id());
        user.getSportList().add(convertToSport(sportDTO, user));
        return userRepository.save(user);
    }

    private User getUser(Integer id) {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("This user with id=" + id + " is not exist!"));
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
    public User updateSport(Integer sport_id, Double burned_calories) {
        log.info("User={} is updating a sport activity with id={} new burned calories={}", SecurityContextHolder.getContext().getAuthentication().getName(),
                sport_id, burned_calories);
        Sport sport = sportRepository.getReferenceById(sport_id);
        sport.setBurnedCalories(burned_calories);
        sportRepository.save(sport);
        return getUser(sport.getUser().getId());
    }

    @Override
    public Sport findSportById(Integer id) {
        log.info("User={} is requesting a sport activity with id={}", SecurityContextHolder.getContext().getAuthentication().getName(), id);
        return sportRepository.findById(id).orElseThrow(() -> new NoSuchElementException("This sport with id=" + id + " is not exist!"));
    }

    @Override
    public void deleteSport(Integer id) {
        log.info("User={} is deleting a sport activity with id={}", SecurityContextHolder.getContext().getAuthentication().getName(), id);
        sportRepository.deleteById(id);
    }
}
