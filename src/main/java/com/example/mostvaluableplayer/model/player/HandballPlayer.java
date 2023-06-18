package com.example.mostvaluableplayer.model.player;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class HandballPlayer extends Player {

    private int goalsMade;
    private int goalsReceived;
}
