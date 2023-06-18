package com.example.mostvaluableplayer.model;

import com.example.mostvaluableplayer.model.player.Player;

import java.util.ArrayList;
import java.util.List;

public class Tournament {

    private List<Game> games = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private Player mostValuablePlayer;
}
