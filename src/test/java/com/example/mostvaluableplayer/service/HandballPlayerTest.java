package com.example.mostvaluableplayer.service;

import com.example.mostvaluableplayer.exception.FailedParsingFileException;
import com.example.mostvaluableplayer.model.player.HandballPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class HandballPlayerTest {

    @Autowired
    @Qualifier("handballPlayerServiceImpl")
    private PlayerService<HandballPlayer> playerService;

    private MultipartFile file;

    @BeforeEach
    void setup() throws IOException {
        InputStream inputStream = BasketballPlayerTest.class.getResourceAsStream("/handballGame1.csv");
        file = new MockMultipartFile("handballGame1.csv", inputStream);
    }

    @Test
    void parseValidUserDataFromCsv() {

        HandballPlayer expectedPlayer = new HandballPlayer();
        expectedPlayer.setName("player 3");
        expectedPlayer.setNickname("nick3");
        expectedPlayer.setNumber(15);
        expectedPlayer.setTeamName("Team A");
        expectedPlayer.setGoalsMade(10);
        expectedPlayer.setGoalsReceived(20);
        expectedPlayer.setRatingPoints(0);

        List<HandballPlayer> players = playerService.parseUserDataFromCsv(file);
        HandballPlayer actualPlayer = players.get(2);

        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    void parseNonValidUserDataFromCsv(){
        InputStream inputStream = BasketballPlayerTest.class.getResourceAsStream("/handballGameInvalidFail.csv");

        assertThrows(FailedParsingFileException.class, () ->playerService.parseUserDataFromCsv(new MockMultipartFile("handballGAmeNonValidFile.csv", inputStream)));
    }

    @Test
    void ratingPointsCount() {

        HandballPlayer expectedPlayer = new HandballPlayer();
        expectedPlayer.setGoalsMade(10);
        expectedPlayer.setGoalsReceived(20);
        expectedPlayer.setRatingPoints(0);

        List<HandballPlayer> players = playerService.parseUserDataFromCsv(file);
        HandballPlayer actualPlayer = players.get(2);

        playerService.ratingPointsCount(actualPlayer);

        assertEquals(expectedPlayer.getRatingPoints(), actualPlayer.getRatingPoints());
    }
}
