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
public class Team {
    private String name;
    private List<? extends Player> players = new ArrayList<>();

    int scoredPoints;
   }
