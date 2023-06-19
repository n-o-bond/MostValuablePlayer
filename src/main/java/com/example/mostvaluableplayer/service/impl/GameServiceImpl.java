package com.example.mostvaluableplayer.service.impl;

import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.Team;
import com.example.mostvaluableplayer.model.player.Player;
import com.example.mostvaluableplayer.service.GameService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("GameServiceImpl")
public class GameServiceImpl implements GameService {

    public Team createTeam(List<? extends Player> players, String teamName) {
        List<? extends Player> playerList = players.stream()
                .filter(p -> p.getTeamName().equals(teamName))
                .toList();
        int scoredPoints =  playerList.stream()
                .mapToInt(Player::getRatingPoints)
                .sum();
        Team team = new Team();
        team.setName(teamName);
        team.setPlayers(playerList);
        team.setScoredPoints(scoredPoints);
        return team;
    }

    @Override
    public Game createGame(List<Team> teams) {
        Set<? extends Player> players = teams.stream()
                .flatMap(team -> team.getPlayers().stream())
                .collect(Collectors.toSet());

        Game game = new Game();
        game.setTeams(teams);
        game.setPlayers(players);
        return game;
    }

    @Override
    public Team determineWinnerTeam(Game game) {
        List<Team> teams = game.getTeams();
        Team winner = teams.stream()
                .max(Comparator.comparingInt(Team::getScoredPoints))
                .orElseThrow();
        game.setWinner(winner);

        addAdditionalRatingPoints(winner);
        return winner;
    }

    @Override
    public void addAdditionalRatingPoints(Team winner) {
        winner.getPlayers()
                .forEach(player -> player.setRatingPoints(player.getRatingPoints()+10));
    }
}
