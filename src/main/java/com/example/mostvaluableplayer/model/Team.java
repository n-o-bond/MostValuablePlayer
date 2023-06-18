package com.example.mostvaluableplayer.model;

import com.example.mostvaluableplayer.model.player.Player;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class Team {

    private String name;
    private List<Player> players = new ArrayList<>();

    public void addPlayer(Player player){
        players.add(player);
        player.setTeamName(this.name);
    }

    public void removePlayer(Player player){
        players.remove(player);
        player.setTeamName(null);
    }

}
