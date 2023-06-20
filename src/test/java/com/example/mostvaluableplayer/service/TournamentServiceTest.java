package com.example.mostvaluableplayer.service;

import com.example.mostvaluableplayer.exception.FailedParsingFileException;
import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.Team;
import com.example.mostvaluableplayer.model.Tournament;
import com.example.mostvaluableplayer.model.player.BasketballPlayer;
import com.example.mostvaluableplayer.model.player.HandballPlayer;
import com.example.mostvaluableplayer.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TournamentServiceTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private TournamentService tournamentService;

    List<Game> games = new ArrayList<>();

    List<MultipartFile> filesValidList = new ArrayList<>();
    List<MultipartFile> filesInvalidList = new ArrayList<>();

    List<BasketballPlayer> basketballPlayersTeamA = new ArrayList<>();

    List<? extends Player> allExpectedPlayers = new ArrayList<>();

    @BeforeEach
    void setup() throws IOException {
        InputStream inputStream = BasketballPlayerTest.class.getResourceAsStream("/basketballGame1.csv");
        InputStream inputStream2 = BasketballPlayerTest.class.getResourceAsStream("/handballGame1.csv");
        InputStream inputStream3 = BasketballPlayerTest.class.getResourceAsStream("/handballGameInValidFile.csv");
        filesValidList.add(new MockMultipartFile("basketballGame1.csv", inputStream));
        filesValidList.add(new MockMultipartFile("handballGame1.csv", inputStream2));

        filesInvalidList.addAll(filesValidList);
        filesInvalidList.add(new MockMultipartFile("handballGameInValidFile.csv", inputStream3));

        basketballPlayersTeamA.add(new BasketballPlayer("player 1", "nick1", 4, "Team A", 10, 2, 7, 29));
        basketballPlayersTeamA.add(new BasketballPlayer("player 2", "nick2", 8, "Team A", 0, 10, 0, 10));
        basketballPlayersTeamA.add(new BasketballPlayer("player 3", "nick3", 15, "Team A", 15, 10, 4, 44));

        List<BasketballPlayer> basketballPlayersTeamB = new ArrayList<>();
        basketballPlayersTeamB.add(new BasketballPlayer("player 4", "nick4", 16, "Team B", 20, 0, 0, 40));
        basketballPlayersTeamB.add(new BasketballPlayer("player 5", "nick5", 23, "Team B", 4, 7, 7, 22));
        basketballPlayersTeamB.add(new BasketballPlayer("player 6", "nick6", 42, "Team B", 8, 10, 0, 26));

        Team basketballTeamA = new Team();
        basketballTeamA.setPlayers(basketballPlayersTeamA);
        basketballTeamA.setScoredPoints(basketballPlayersTeamA.stream()
                .mapToInt(Player::getRatingPoints)
                .sum());
        basketballTeamA.setName("Team A");

        Team basketballTeamB = new Team();
        basketballTeamB.setPlayers(basketballPlayersTeamB);
        basketballTeamB.setScoredPoints(basketballPlayersTeamB.stream()
                .mapToInt(Player::getRatingPoints)
                .sum());
        basketballTeamB.setName("Team B");

        List<HandballPlayer> handballPlayersTeamA = new ArrayList<>();
        handballPlayersTeamA.add(new HandballPlayer("player 1", "nick1", 4, "Team A", 0, 20, -20));
        handballPlayersTeamA.add(new HandballPlayer("player 2", "nick2", 8, "Team A", 15, 20, 10));
        handballPlayersTeamA.add(new HandballPlayer("player 3", "nick3", 15, "Team A", 10, 20, 0));

        List<HandballPlayer> handballPlayersTeamB = new ArrayList<>();
        handballPlayersTeamB.add(new HandballPlayer("player 4", "nick4", 16, "Team B", 1, 25, -23));
        handballPlayersTeamB.add(new HandballPlayer("player 5", "nick5", 23, "Team B", 12, 25, -1));
        handballPlayersTeamB.add(new HandballPlayer("player 6", "nick6", 42, "Team B", 8, 25, -9));

        Team handballTeamA = new Team();
        handballTeamA.setPlayers(handballPlayersTeamA);
        handballTeamA.setScoredPoints(handballPlayersTeamA.stream()
                .mapToInt(Player::getRatingPoints)
                .sum());
        handballTeamA.setName("Team A");

        Team handballTeamB = new Team();
        handballTeamB.setPlayers(handballPlayersTeamB);
        handballTeamB.setScoredPoints(handballPlayersTeamB.stream()
                .mapToInt(Player::getRatingPoints)
                .sum());
        handballTeamB.setName("Team B");

        Game basketballGame = gameService.createGame( List.of(basketballTeamA, basketballTeamB));
        Game handballGame = gameService.createGame( List.of(handballTeamA, handballTeamB));

        games = List.of(basketballGame, handballGame);

        allExpectedPlayers = games.stream()
                .flatMap(game -> game.getPlayers().stream())
                .toList();
    }

    @Test
    void createTournament(){
        Tournament expectedTournament = new Tournament();
        expectedTournament.setGames(games);
        expectedTournament.setPlayers(allExpectedPlayers);
        Player MVP = basketballPlayersTeamA.get(2);
        expectedTournament.setMostValuablePlayer(MVP);

        Tournament actualTournament = tournamentService.createTournament(games);

        assertEquals(expectedTournament, actualTournament);
    }

    @Test
    void determineMostValuablePlayer(){
        Player expectedMVP = basketballPlayersTeamA.get(2);
        Player actualMVP = tournamentService.determineMostValuablePlayer(allExpectedPlayers);

        assertEquals(expectedMVP, actualMVP);
    }

    @Test
    void getTournamentFromValidFiles(){
        Tournament expectedTournament = new Tournament();
        expectedTournament.setGames(games);
        expectedTournament.setPlayers(allExpectedPlayers);
        Player MVP = basketballPlayersTeamA.get(2);
        expectedTournament.setMostValuablePlayer(MVP);

        Tournament actualTournament = tournamentService.getTournamentFromFiles(filesValidList);

        assertEquals(expectedTournament, actualTournament);
    }

    @Test
    void getTournamentFromInvalidFiles(){

        assertThrows(FailedParsingFileException.class,() -> tournamentService.getTournamentFromFiles(filesInvalidList));
    }
}
