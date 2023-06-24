package com.example.mostvaluableplayer.service;

import com.example.mostvaluableplayer.exception.FailedParsingFileException;
import com.example.mostvaluableplayer.model.SportType;
import com.example.mostvaluableplayer.model.player.BasketballPlayer;
import com.example.mostvaluableplayer.model.player.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
class BasketballPlayerTest {

    @Autowired
    private List<PlayerService<? extends Player>> playerServices;

    @Test
    void parseValidUserDataFromCsv() throws IOException {
        //GIVEN
        InputStream inputStream = BasketballPlayerTest.class.getResourceAsStream("/basketballGame1.csv");
        MultipartFile file = new MockMultipartFile("basketballGame1.csv", inputStream);

        BasketballPlayer expectedPlayer = new BasketballPlayer();
        expectedPlayer.setName("player 2");
        expectedPlayer.setNickname("nick2");
        expectedPlayer.setNumber(8);
        expectedPlayer.setTeamName("Team A");
        expectedPlayer.setScoredPoints(0);
        expectedPlayer.setRebounds(10);
        expectedPlayer.setAssists(0);
        expectedPlayer.setRatingPoints(10);
        //WHEN
        List<? extends Player> players = playerServices.stream()
                .filter(p -> p.getSportType().equals(SportType.BASKETBALL))
                .findFirst()
                .get()
                .parseUserDataFromCsv(file);
        BasketballPlayer actualPlayer = (BasketballPlayer) players.get(1);
        //THEN
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    void parseInvalidUserDataFromCsv() {
        //GIVEN
        InputStream inputStream = BasketballPlayerTest.class.getResourceAsStream("/basketballGameInvalidFail.csv");
        //WHEN & THEN
        assertThrows(FailedParsingFileException.class, () -> playerServices.stream()
                .filter(p -> p.getSportType().equals(SportType.BASKETBALL))
                .findFirst()
                .get()
                .parseUserDataFromCsv(new MockMultipartFile("basketballGameInvalidFail.csv", inputStream)));
    }
}
