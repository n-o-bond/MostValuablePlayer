package com.example.mostvaluableplayer.model.player;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class BasketballPlayer extends Player {
    private int scoredPoints;
    private int rebounds;
    private int assists;

    public BasketballPlayer(String name, String nickname, int number, String teamName, int scoredPoints, int rebounds, int assists, int ratingPoints) {
        setName(name);
        setNickname(nickname);
        setNumber(number);
        setTeamName(teamName);
        this.scoredPoints = scoredPoints;
        this.rebounds = rebounds;
        this.assists = assists;
        setRatingPoints(ratingPoints);
    }
}
