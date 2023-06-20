package com.example.mostvaluableplayer.model;

import com.example.mostvaluableplayer.model.player.Player;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Game {
    private List<Team> teams = new ArrayList<>();
    private Set<? extends Player> players = new HashSet<>();
    private Team winner;
}
