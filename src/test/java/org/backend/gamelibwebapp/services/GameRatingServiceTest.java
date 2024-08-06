package org.backend.gamelibwebapp.services;

import org.backend.gamelibwebapp.repositories.AppUserRepository;
import org.backend.gamelibwebapp.repositories.GameRatingRepository;
import org.backend.gamelibwebapp.repositories.GameRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GameRatingServiceTest {


    private GameRatingService underTestService;

    @Mock
    private GameRatingRepository gameRatingRepository;
    @Mock
    private GameRepository gameRepository;
    @Mock
    private AppUserRepository appUserRepository;


    @BeforeEach
    void setUp() {
        underTestService = new GameRatingService(appUserRepository,gameRepository,gameRatingRepository);
    }

    @Test
    void rateGame() {

    }

    @Test
    void getAverageRating() {
    }

    @Test
    void checkIfRated() {
    }
}