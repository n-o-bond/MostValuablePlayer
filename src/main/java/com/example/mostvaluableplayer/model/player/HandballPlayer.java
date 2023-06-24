package com.example.mostvaluableplayer.model.player;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class HandballPlayer extends Player {
    private int goalsMade;
    private int goalsReceived;
}
