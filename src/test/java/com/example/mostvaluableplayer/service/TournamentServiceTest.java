package com.example.mostvaluableplayer.service;

import com.example.mostvaluableplayer.exception.FailedParsingFileException;
import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.Tournament;
import com.example.mostvaluableplayer.model.player.BasketballPlayer;
import com.example.mostvaluableplayer.model.player.HandballPlayer;
import com.example.mostvaluableplayer.model.player.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TournamentServiceTest {
    @Autowired
    private TournamentService tournamentService;
    @Autowired
    private GameService gameService;
    @Autowired
    private PlayerService<BasketballPlayer> basketballPlayerService;
    @Autowired
    private PlayerService<HandballPlayer> handballPlayerService;

    @Test
    void createTournamentTest() throws IOException {
        InputStream inputStreamForBasketballGame = BasketballPlayerTest.class.getResourceAsStream("/basketballGame1.csv");
        InputStream inputStreamForHandballGame = BasketballPlayerTest.class.getResourceAsStream("/handballGame1.csv");
        MultipartFile basketballFile = new MockMultipartFile("basketballGame1.csv", inputStreamForBasketballGame);
        MultipartFile handballFile = new MockMultipartFile("handballGame1.csv", inputStreamForHandballGame);
        List<BasketballPlayer> basketballPlayers = basketballPlayerService.parseUserDataFromCsv(basketballFile);
        List<HandballPlayer> handballPlayers = handballPlayerService.parseUserDataFromCsv(handballFile);

        Game basketballGame = gameService.createGameFromPlayers(basketballPlayers);
        Game handballGame = gameService.createGameFromPlayers(handballPlayers);

        List<Game> games = List.of(basketballGame, handballGame);
        List<? extends Player> allPlayers = games.stream()
                .flatMap(game -> game.getPlayers().stream())
                .toList();

        Tournament expectTournament = new Tournament();
        expectTournament.setGames(games);
        expectTournament.setPlayers(allPlayers);
        expectTournament.setMostValuablePlayer(basketballPlayers.get(2));

        Tournament actualTournament = tournamentService.createTournament(games);

        assertEquals(expectTournament, actualTournament);
    }

    @Test
    void determineMostValuablePlayerTest() throws IOException {
        InputStream inputStreamForBasketballGame = BasketballPlayerTest.class.getResourceAsStream("/basketballGame1.csv");
        InputStream inputStreamForHandballGame = BasketballPlayerTest.class.getResourceAsStream("/handballGame1.csv");
        MultipartFile basketballFile = new MockMultipartFile("basketballGame1.csv", inputStreamForBasketballGame);
        MultipartFile handballFile = new MockMultipartFile("handballGame1.csv", inputStreamForHandballGame);
        List<? extends Player> basketballPlayers = basketballPlayerService.parseUserDataFromCsv(basketballFile);
        List<? extends Player> handballPlayers = handballPlayerService.parseUserDataFromCsv(handballFile);
        List<Player> allPlayers = new ArrayList<>();
        allPlayers.addAll(basketballPlayers);
        allPlayers.addAll(handballPlayers);

        Player expectedMVP = basketballPlayers.get(2);
        Player actualMVP = tournamentService.determineMostValuablePlayer(allPlayers);

        assertEquals(expectedMVP, actualMVP);
    }

    @Test
    void getTournamentFromValidFilesTest() throws IOException {
        List<MultipartFile> filesValidList = new ArrayList<>();
        InputStream inputStreamForBasketballGame = BasketballPlayerTest.class.getResourceAsStream("/basketballGame1.csv");
        InputStream inputStreamForHandballGame = BasketballPlayerTest.class.getResourceAsStream("/handballGame1.csv");
        MultipartFile basketballFile = new MockMultipartFile("basketballGame1.csv", inputStreamForBasketballGame);
        MultipartFile handballFile = new MockMultipartFile("handballGame1.csv", inputStreamForHandballGame);
        filesValidList.add(basketballFile);
        filesValidList.add(handballFile);
        List<BasketballPlayer> basketballPlayers = basketballPlayerService.parseUserDataFromCsv(basketballFile);
        List<HandballPlayer> handballPlayers = handballPlayerService.parseUserDataFromCsv(handballFile);

        Game basketballGame = gameService.createGameFromPlayers(basketballPlayers);
        Game handballGame = gameService.createGameFromPlayers(handballPlayers);

        List<Game> games = List.of(basketballGame, handballGame);
        List<? extends Player> allPlayers = games.stream()
                .flatMap(game -> game.getPlayers().stream())
                .toList();

        Tournament expectedTournament = new Tournament();
        expectedTournament.setGames(games);
        expectedTournament.setPlayers(allPlayers);
        expectedTournament.setMostValuablePlayer(basketballPlayers.get(2));

        Tournament actualTournament = tournamentService.getTournamentFromFiles(filesValidList);

        assertEquals(expectedTournament, actualTournament);
    }

    @Test
    void getTournamentFromInvalidFilesTest() throws IOException {
        List<MultipartFile> filesInvalidList = new ArrayList<>();
        InputStream inputStreamForBasketballGame = BasketballPlayerTest.class.getResourceAsStream("/basketballGame1.csv");
        InputStream inputStreamForHandballGame = BasketballPlayerTest.class.getResourceAsStream("/handballGame1.csv");
        InputStream inputStreamForInvalidGame = BasketballPlayerTest.class.getResourceAsStream("/handballGameInValidFile.csv");
        filesInvalidList.add(new MockMultipartFile("basketballGame1.csv", inputStreamForBasketballGame));
        filesInvalidList.add(new MockMultipartFile("handballGame1.csv", inputStreamForHandballGame));
        filesInvalidList.add(new MockMultipartFile("/handballGameInValidFile.csv", inputStreamForInvalidGame));

        assertThrows(FailedParsingFileException.class, () -> tournamentService.getTournamentFromFiles(filesInvalidList));
    }
}