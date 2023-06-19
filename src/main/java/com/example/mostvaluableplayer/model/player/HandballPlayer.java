package com.example.mostvaluableplayer.model.player;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class HandballPlayer extends Player {
    private int goalsMade;
    private int goalsReceived;

    public HandballPlayer(String name, String nickname, int number, String teamName, int goalsMade, int goalsReceived, int ratingPoints) {
        setName(name);
        setNickname(nickname);
        setNumber(number);
        setTeamName(teamName);
        this.goalsMade = goalsMade;
        this.goalsReceived = goalsReceived;
        setRatingPoints(ratingPoints);
    }
}
