package com.example.mostvaluableplayer.service.impl;

import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.Tournament;
import com.example.mostvaluableplayer.model.player.Player;
import com.example.mostvaluableplayer.service.TournamentService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("TournamentServiceImpl")
public class TournamentServiceImpl implements TournamentService {

    @Override
    public Tournament createTournament(List<Game> games) {
        List<Player> players = games.stream()
                .flatMap(game -> game.getPlayers().stream())
                .toList();
        Tournament tournament = new Tournament();
        tournament.setGames(games);
        tournament.setPlayers(players);
        return tournament;
    }

    @Override
    public Player determineMostValuablePlayer(List<Player> players) {
        Optional<Player> playerWithMaxRatingPoints = players.stream()
                .collect(Collectors.groupingBy(Player::getNickname, Collectors.summingInt(Player::getRatingPoints)))
                .entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(entry -> players.stream()
                        .filter(player -> player.getNickname().equals(entry.getKey()))
                        .max(Comparator.comparingInt(Player::getRatingPoints))
                        .orElseThrow());


        return playerWithMaxRatingPoints.get();
    }
}
