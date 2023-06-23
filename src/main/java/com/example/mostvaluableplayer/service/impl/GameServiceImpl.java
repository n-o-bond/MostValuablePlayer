package com.example.mostvaluableplayer.service.impl;

import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.Team;
import com.example.mostvaluableplayer.model.player.Player;
import com.example.mostvaluableplayer.service.GameService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    @Override
    public Game createGameFromPlayers(List<? extends Player> players) {
        List<String> teamNames = players.stream()
                .map(Player::getTeamName)
                .distinct()
                .toList();

        List<Team> teams = teamNames.stream()
                .map(name -> createTeam(players, name))
                .toList();

        Game game = new Game();
        game.setTeams(teams);
        game.setPlayers(new HashSet<>(players));
        game.setWinner(determineWinnerTeam(game));
        return game;
    }

    private Team createTeam(List<? extends Player> players, String teamName) {
        List<? extends Player> playerList = players.stream()
                .filter(p -> p.getTeamName().equals(teamName))
                .toList();
        int scoredPoints =  playerList.stream()
                .mapToInt(Player::getPointsForTeam)
                .sum();
        Team team = new Team();
        team.setName(teamName);
        team.setPlayers(playerList);
        team.setScoredPoints(scoredPoints);
        return team;
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

    private void addAdditionalRatingPoints(Team winner) {
        winner.getPlayers()
                .forEach(player -> player.setRatingPoints(player.getRatingPoints()+10));
    }
}
