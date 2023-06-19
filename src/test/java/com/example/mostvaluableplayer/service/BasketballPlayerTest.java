package com.example.mostvaluableplayer.service;

import com.example.mostvaluableplayer.exception.FailedParsingFileException;
import com.example.mostvaluableplayer.model.player.BasketballPlayer;
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
class BasketballPlayerTest {

    @Autowired
    @Qualifier("BasketballPlayerServiceImpl")
    private PlayerService<BasketballPlayer> playerService;

    private MultipartFile file;

    @BeforeEach
    void setup() throws IOException {
        InputStream inputStream = BasketballPlayerTest.class.getResourceAsStream("/basketballGame1.csv");
        file = new MockMultipartFile("basketballGame1.csv", inputStream);
    }

    @Test
    void parseValidUserDataFromCsv() {

        BasketballPlayer expectedPlayer = new BasketballPlayer();
        expectedPlayer.setName("player 2");
        expectedPlayer.setNickname("nick2");
        expectedPlayer.setNumber(8);
        expectedPlayer.setTeamName("Team A");
        expectedPlayer.setScoredPoints(0);
        expectedPlayer.setRebounds(10);
        expectedPlayer.setAssists(0);
        expectedPlayer.setRatingPoints(10);

        List<BasketballPlayer> players = playerService.parseUserDataFromCsv(file);
        BasketballPlayer actualPlayer = players.get(1);

        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    void parseNonValidUserDataFromCsv() {
        InputStream inputStream = BasketballPlayerTest.class.getResourceAsStream("/basketballGameNonValidFail.csv");

        assertThrows(FailedParsingFileException.class, () -> playerService.parseUserDataFromCsv(new MockMultipartFile("basketballGameNonValidFail.csv", inputStream)));
    }

    @Test
    void ratingPointsCount() {

        BasketballPlayer expectedPlayer = new BasketballPlayer();
        expectedPlayer.setScoredPoints(0);
        expectedPlayer.setRebounds(10);
        expectedPlayer.setAssists(0);
        expectedPlayer.setRatingPoints(10);

        List<BasketballPlayer> players = playerService.parseUserDataFromCsv(file);
        BasketballPlayer actualPlayer = players.get(1);

        playerService.ratingPointsCount(actualPlayer);

        assertEquals(expectedPlayer.getRatingPoints(), actualPlayer.getRatingPoints());
    }

}
