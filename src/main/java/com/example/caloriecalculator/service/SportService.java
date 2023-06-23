package com.example.caloriecalculator.service;

import com.example.caloriecalculator.dto.SportDTO;
import com.example.caloriecalculator.model.Sport;
import com.example.caloriecalculator.model.User;
import com.example.caloriecalculator.repositories.SportRepository;
import com.example.caloriecalculator.repositories.UserRepository;;
import com.example.caloriecalculator.service.interfaces.ISportService;
import com.mysql.cj.exceptions.NumberOutOfRange;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiConsumer;

@Service
@AllArgsConstructor
@Slf4j
public class SportService implements ISportService {

    private SportRepository sportRepository;
    private UserRepository userRepository;

    @Override
    public User saveUsersSport(SportDTO sportDTO) {
        log.info("User={} is saving a new sport activity={}", getAuthenticatedUsername(), sportDTO);
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
        log.info("User={} is updating a sport activity with id={} new burned calories={}", getAuthenticatedUsername(),
                sport_id, burned_calories);
        if (burned_calories <= 0) {
            throw new NumberOutOfRange("The calories has to be greater than 0!");
        }
        User user = userRepository.findUserBySport(sport_id);
        BiConsumer<Double, Sport> consumer = (burnedCal, sport) -> sport.setBurnedCalories(burnedCal);
        Sport sport = user.getSportList().stream().filter(sport1 -> Objects.equals(sport_id, sport1.getId()))
                .findAny().orElseThrow(() -> new NoSuchElementException("This sport with id=" + sport_id + " is not exist!"));
        consumer.accept(burned_calories, sport);
        return userRepository.save(user);
    }

    @Override
    public Sport findSportById(Integer id) {
        log.info("User={} is requesting a sport activity with id={}", getAuthenticatedUsername(), id);
        return sportRepository.findById(id).orElseThrow(() -> new NoSuchElementException("This sport with id=" + id + " is not exist!"));
    }

    @Override
    public void deleteSport(Integer id) {
        log.info("User={} is deleting a sport activity with id={}", getAuthenticatedUsername(), id);
        if (sportRepository.existsById(id)) {
            sportRepository.deleteById(id);
            log.info("Deleting has successfully finished!");
        } else {
            throw new NoSuchElementException("Sport activity with id=" + id + " is not exist!");
        }
    }

    private String getAuthenticatedUsername() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return SecurityContextHolder.getContext().getAuthentication().getName();
        }
        return "anonymous";
    }
}
