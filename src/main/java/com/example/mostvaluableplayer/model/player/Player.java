package com.example.mostvaluableplayer.model.player;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
public abstract class Player {
    protected String name;
    protected String nickname;
    protected int number;
    protected String teamName;
    protected int ratingPoints;
    protected int pointsForTeam;
}
