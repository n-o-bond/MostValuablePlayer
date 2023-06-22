package com.example.mostvaluableplayer.service.impl;

import com.example.mostvaluableplayer.exception.FailedParsingFileException;
import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.SportType;
import com.example.mostvaluableplayer.model.Tournament;
import com.example.mostvaluableplayer.model.player.Player;
import com.example.mostvaluableplayer.service.GameService;
import com.example.mostvaluableplayer.service.PlayerService;
import com.example.mostvaluableplayer.service.TournamentService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TournamentServiceImpl implements TournamentService {
    private final GameService gameService;

    private final List<PlayerService<? extends Player>> playerServices;

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
            List<? extends Player> players = playerServices.stream()
                    .filter(p -> p.getSportType().equals(sportType))
                    .findFirst().get().parseUserDataFromCsv(file);
            games.add(gameService.createGameFromPlayers(players));
        }

        return createTournament(games);
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
