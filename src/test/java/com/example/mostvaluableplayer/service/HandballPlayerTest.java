package com.example.mostvaluableplayer.service;

import com.example.mostvaluableplayer.exception.FailedParsingFileException;
import com.example.mostvaluableplayer.model.SportType;
import com.example.mostvaluableplayer.model.player.HandballPlayer;
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
class HandballPlayerTest {

    @Autowired
    private List<PlayerService<? extends Player>> playerServices;

    @Test
    void parseValidUserDataFromCsv() throws IOException {
        //GIVEN
        InputStream inputStream = BasketballPlayerTest.class.getResourceAsStream("/handballGame1.csv");
        MultipartFile file = new MockMultipartFile("handballGame1.csv", inputStream);

        HandballPlayer expectedPlayer = new HandballPlayer();
        expectedPlayer.setName("player 3");
        expectedPlayer.setNickname("nick3");
        expectedPlayer.setNumber(15);
        expectedPlayer.setTeamName("Team A");
        expectedPlayer.setGoalsMade(10);
        expectedPlayer.setGoalsReceived(20);
        expectedPlayer.setRatingPoints(0);

        //WHEN
        List<? extends Player> players = playerServices.stream()
                .filter(p -> p.getSportType().equals(SportType.HANDBALL))
                .findFirst()
                .get()
                .parseUserDataFromCsv(file);
        HandballPlayer actualPlayer = (HandballPlayer) players.get(2);
        //THEN
        assertEquals(expectedPlayer, actualPlayer);
    }

    @Test
    void parseInvalidUserDataFromCsv(){
        //GIVEN
        InputStream inputStream = BasketballPlayerTest.class.getResourceAsStream("/handballGameInvalidFail.csv");
        //WHEN & THEN
        assertThrows(FailedParsingFileException.class, () -> playerServices.stream()
                .filter(p -> p.getSportType().equals(SportType.HANDBALL))
                .findFirst()
                .get()
                .parseUserDataFromCsv(new MockMultipartFile("handballGAmeNonValidFile.csv", inputStream)));
    }
}
