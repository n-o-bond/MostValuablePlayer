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

    public BasketballPlayer(String name, String nickname, int number, String teamName, int scoredPoints, int rebounds, int assists, int ratingPoints) {
        this.name = name;
        this.nickname = nickname;
        this.number = number;
        this.teamName = teamName;
        this.scoredPoints = scoredPoints;
        this.rebounds = rebounds;
        this.assists = assists;
        this.ratingPoints = ratingPoints;
    }
}
