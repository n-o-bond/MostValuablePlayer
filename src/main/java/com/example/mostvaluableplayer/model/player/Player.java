package com.example.mostvaluableplayer.model.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Player {

    String name;
    String nickname;
    int number;
    String teamName;
    int additionalRatingPoints;

}
