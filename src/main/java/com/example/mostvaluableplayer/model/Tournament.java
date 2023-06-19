package com.example.mostvaluableplayer.model;

import com.example.mostvaluableplayer.model.player.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Tournament {

    private List<Game> games = new ArrayList<>();
    private List<Player> players = new ArrayList<>();
    private Player mostValuablePlayer;
}
