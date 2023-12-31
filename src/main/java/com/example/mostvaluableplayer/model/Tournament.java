package com.example.mostvaluableplayer.model;

import com.example.mostvaluableplayer.model.player.Player;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Tournament {
    private List<Game> games = new ArrayList<>();
    private List<? extends Player> players = new ArrayList<>();
    private Player mostValuablePlayer;
}
