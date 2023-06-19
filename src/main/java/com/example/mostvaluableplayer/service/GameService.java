package com.example.mostvaluableplayer.service;

import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.Team;
import com.example.mostvaluableplayer.model.player.Player;

import java.util.List;

public interface GameService {

    Team createTeam(List<? extends Player> players, String teamName);

    Game createGame(List<Team> teams);

    Team determineWinnerTeam(Game game);

    void addAdditionalRatingPoints(Team winner);
}
