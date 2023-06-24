package com.example.mostvaluableplayer.model.player;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BasketballPlayer extends Player {
    private int scoredPoints;
    private int rebounds;
    private int assists;
}
