package com.example.mostvaluableplayer.service;

import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.Team;
import com.example.mostvaluableplayer.model.player.BasketballPlayer;
import com.example.mostvaluableplayer.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class GameServiceTest {

    @Autowired
    @Qualifier("basketballPlayerServiceImpl")
    private PlayerService<BasketballPlayer> playerService;

    @Autowired
    private GameService gameService;

    private MultipartFile file;
    List<BasketballPlayer> playersTeamA;
    List<BasketballPlayer> playersTeamB;
    Team expectedTeamA;
    Team expectedTeamB;

    @BeforeEach
    void setup() throws IOException {
        InputStream inputStream = BasketballPlayerTest.class.getResourceAsStream("/basketballGame1.csv");
        file = new MockMultipartFile("basketballGame1.csv", inputStream);

        playersTeamA = new ArrayList<>();
        playersTeamA.add(new BasketballPlayer("player 1", "nick1", 4, "Team A", 10, 2, 7, 29));
        playersTeamA.add(new BasketballPlayer("player 2", "nick2", 8, "Team A", 0, 10, 0, 10));
        playersTeamA.add(new BasketballPlayer("player 3", "nick3", 15, "Team A", 15, 10, 4, 44));

        playersTeamB = new ArrayList<>();
        playersTeamB.add(new BasketballPlayer("player 4", "nick4", 16, "Team B", 20, 0, 0, 40));
        playersTeamB.add(new BasketballPlayer("player 5", "nick5", 23, "Team B", 4, 7, 7, 22));
        playersTeamB.add(new BasketballPlayer("player 6", "nick6", 42, "Team B", 8, 10, 0, 26));

        expectedTeamA = new Team();
        expectedTeamA.setPlayers(playersTeamA);
        expectedTeamA.setScoredPoints(playersTeamA.stream()
                .mapToInt(Player::getRatingPoints)
                .sum());
        expectedTeamA.setName("Team A");

        expectedTeamB = new Team();
        expectedTeamB.setPlayers(playersTeamB);
        expectedTeamB.setScoredPoints(playersTeamB.stream()
                .mapToInt(Player::getRatingPoints)
                .sum());
        expectedTeamB.setName("Team B");
    }

    @Test
    void createValidTeam() {
        List<BasketballPlayer> players = playerService.parseUserDataFromCsv(file);
        Team actualTeamA = gameService.createTeam(players, "Team A");
        Team actualTeamB = gameService.createTeam(players, "Team B");

        assertEquals(expectedTeamA, actualTeamA);
        assertEquals(expectedTeamB, actualTeamB);
    }

    @Test
    void createValidGame() {
        List<Team> teams = List.of(expectedTeamA, expectedTeamB);

        List<BasketballPlayer> players = new ArrayList<>(playersTeamA);
        players.addAll(playersTeamB);
        Set<BasketballPlayer> expectedPlayers = new HashSet<>(players);

        Game expectedGame = new Game();
        expectedGame.setTeams(teams);
        expectedGame.setPlayers(expectedPlayers);
        expectedGame.setWinner(expectedTeamB);

        Game actualGame = gameService.createGame(teams);

        assertEquals(expectedGame, actualGame);
    }

    @Test
    void determineWinnerTeam() {
        List<Team> teams = List.of(expectedTeamA, expectedTeamB);

        Team expectedWinner = expectedTeamB;

        expectedWinner.getPlayers()
                .forEach(player -> player.setRatingPoints(player.getRatingPoints() + 10));

        Game actualGame = gameService.createGame(teams);
        Team actualWinner = gameService.determineWinnerTeam(actualGame);

        assertEquals(expectedWinner, actualWinner);
    }
}
