package com.example.mostvaluableplayer.service;

import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.Team;
import com.example.mostvaluableplayer.model.player.BasketballPlayer;
import com.example.mostvaluableplayer.model.player.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class GameServiceTest {

    @Autowired
    private PlayerService<BasketballPlayer> basketballPlayerService;

    @Autowired
    private GameService gameService;

    @Test
    void createGameFromPlayersTest() throws IOException {
        //GIVEN
        InputStream inputStreamForBasketballGame = BasketballPlayerTest.class.getResourceAsStream("/basketballGame1.csv");
        List<BasketballPlayer> basketballPlayers = basketballPlayerService
                .parseUserDataFromCsv(new MockMultipartFile("basketballGame1.csv", inputStreamForBasketballGame));

        List<BasketballPlayer> playersTeamA = basketballPlayers.stream()
                .filter(p -> p.getTeamName().equals("Team A"))
                .toList();
        List<BasketballPlayer> playersTeamB = basketballPlayers.stream()
                .filter(p -> p.getTeamName().equals("Team B"))
                .toList();

        Team expectedTeamA = new Team();
        expectedTeamA.setPlayers(playersTeamA);
        expectedTeamA.setScoredPoints(playersTeamA.stream()
                .mapToInt(Player::getPointsForTeam)
                .sum());
        expectedTeamA.setName("Team A");

        Team expectedTeamB = new Team();
        expectedTeamB.setPlayers(playersTeamB);
        expectedTeamB.setScoredPoints(playersTeamB.stream()
                .mapToInt(Player::getPointsForTeam)
                .sum());
        expectedTeamB.setName("Team B");

        Game expectedGame = new Game();
        expectedGame.setPlayers(new HashSet<>(basketballPlayers));
        expectedGame.setTeams(List.of(expectedTeamA, expectedTeamB));
        expectedGame.setWinner(expectedTeamB);
        //WHEN
        Game actualGame = gameService.createGameFromPlayers(basketballPlayers);
        //THEN
        assertEquals(expectedGame, actualGame);
    }

    @Test
    void determineWinnerTeamTest() throws IOException {
        //GIVEN
        InputStream inputStreamForBasketballGame = BasketballPlayerTest.class.getResourceAsStream("/basketballGame1.csv");
        List<BasketballPlayer> expectedBasketballPlayers = basketballPlayerService
                .parseUserDataFromCsv(new MockMultipartFile("basketballGame1.csv", inputStreamForBasketballGame));

        List<BasketballPlayer> playersTeamB = expectedBasketballPlayers.stream()
                .filter(p -> p.getTeamName().equals("Team B"))
                .toList();

        Team expectedWinner = new Team();
        expectedWinner.setPlayers(playersTeamB);
        expectedWinner.setScoredPoints(playersTeamB.stream()
                .mapToInt(Player::getPointsForTeam)
                .sum());
        expectedWinner.setName("Team B");

        playersTeamB.forEach(player -> {
            int newRating = player.getRatingPoints() + 10;
            player.setRatingPoints(newRating);
        });

        InputStream inputStreamForActualGame = BasketballPlayerTest.class.getResourceAsStream("/basketballGame1.csv");
        List<BasketballPlayer> actualBasketballPlayers = basketballPlayerService
                .parseUserDataFromCsv(new MockMultipartFile("basketballGame1.csv", inputStreamForActualGame));

        //WHEN
        Game actualGame = gameService.createGameFromPlayers(actualBasketballPlayers);
        Team actualWinner = actualGame.getWinner();
        //THEN
        assertEquals(expectedWinner, actualWinner);
        assertEquals(playersTeamB.get(0).getRatingPoints(), actualWinner.getPlayers().get(0).getRatingPoints());
        assertEquals(playersTeamB.get(1), actualWinner.getPlayers().get(1));
    }
}
