package com.example.mostvaluableplayer.service;

import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.Team;
import com.example.mostvaluableplayer.model.player.Player;

import java.util.List;

public interface GameService {

    Game createGameFromPlayers(List<? extends Player> players);

    Team determineWinnerTeam(Game game);
}
