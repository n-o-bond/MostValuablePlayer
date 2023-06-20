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

    public HandballPlayer(String name, String nickname, int number, String teamName, int goalsMade, int goalsReceived, int ratingPoints) {
        this.name = name;
        this.nickname = nickname;
        this.number = number;
        this.teamName = teamName;
        this.goalsMade = goalsMade;
        this.goalsReceived = goalsReceived;
        this.ratingPoints = ratingPoints;
    }
}
