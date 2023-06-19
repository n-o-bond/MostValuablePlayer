package com.example.mostvaluableplayer.service;

import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.Team;
import com.example.mostvaluableplayer.model.player.Player;

import java.util.List;

public interface GameService {

    Team createTeam(List<Player> players, String teamName);

    Game createGame(List<Team> teams);

    void determineWinnerTeam(Game game);
}
