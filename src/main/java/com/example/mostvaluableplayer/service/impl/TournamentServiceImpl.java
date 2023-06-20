package com.example.mostvaluableplayer.service.impl;

import com.example.mostvaluableplayer.exception.FailedParsingFileException;
import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.SportType;
import com.example.mostvaluableplayer.model.Team;
import com.example.mostvaluableplayer.model.Tournament;
import com.example.mostvaluableplayer.model.player.Player;
import com.example.mostvaluableplayer.service.GameService;
import com.example.mostvaluableplayer.service.PlayerService;
import com.example.mostvaluableplayer.service.TournamentService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {
    private final BasketballPlayerServiceImpl basketballPlayerService;

    private final HandballPlayerServiceImpl handballPlayerService;
    private final GameService gameService;

    private final Map<SportType, PlayerService<? extends Player>> playerServices = new HashMap<>();


    @PostConstruct
    private void initializePlayerServices() {
        playerServices.put(SportType.BASKETBALL, basketballPlayerService);
        playerServices.put(SportType.HANDBALL, handballPlayerService);
    }

    @Override
    public Tournament createTournament(List<Game> games) {
        List<? extends Player> players = games.stream()
                .flatMap(game -> game.getPlayers().stream())
                .toList();
        Player MVP = determineMostValuablePlayer(players);

        Tournament tournament = new Tournament();
        tournament.setGames(games);
        tournament.setPlayers(players);
        tournament.setMostValuablePlayer(MVP);
        return tournament;
    }

    @Override
    public Player determineMostValuablePlayer(List<? extends Player> players) {
        Player playerWithMaxRatingPoints = players.stream()
                .collect(Collectors.groupingBy(Player::getNickname, Collectors.summingInt(Player::getRatingPoints)))
                .entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(entry -> players.stream()
                        .filter(player -> player.getNickname().equals(entry.getKey()))
                        .max(Comparator.comparingInt(Player::getRatingPoints))
                        .orElseThrow())
                .get();

        return playerWithMaxRatingPoints;
    }

    @Override
    public Tournament getTournamentFromFiles(List<MultipartFile> files) {
        List<Game> games = new ArrayList<>();

        for (MultipartFile file : files) {
            SportType sportType = getHeader(file);
            List<? extends Player> players = getPlayers(sportType, file);
            Game game = getGame(players);
            games.add(game);
        }

        return createTournament(games);
    }

    private Game getGame(List<? extends Player> players) {
        List<String> teamNames = players.stream()
                .map(Player::getTeamName)
                .distinct()
                .toList();

        List<Team> teams = teamNames.stream()
                .map(name -> gameService.createTeam(players, name))
                .toList();

        return gameService.createGame(teams);
    }

    private List<? extends Player> getPlayers(SportType sportType, MultipartFile file) {
        PlayerService<? extends Player> playerService = playerServices.get(sportType);
        if (playerService == null) {
            throw new IllegalArgumentException("Unsupported sport type: " + sportType);
        }

        return playerService.parseUserDataFromCsv(file);
    }

    private SportType getHeader(MultipartFile multipartFile) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8))) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            return SportType.valueOf(parser.getHeaderMap().keySet().stream().findFirst().orElse(null));
        } catch (NullPointerException e) {
            throw new FailedParsingFileException("Header can not be null");
        } catch (IOException e) {
            throw new FailedParsingFileException("Failed to access CSV file");
        }
    }
}
