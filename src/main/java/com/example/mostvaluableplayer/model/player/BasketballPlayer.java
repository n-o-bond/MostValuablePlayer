package com.example.mostvaluableplayer.model.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BasketballPlayer extends Player {

    private int scoredPoints;
    private int rebounds;
    private int assists;

}
