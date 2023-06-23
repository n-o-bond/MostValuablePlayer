package com.example.mostvaluableplayer.service.impl;

import com.example.mostvaluableplayer.model.SportType;
import com.example.mostvaluableplayer.model.player.BasketballPlayer;
import com.example.mostvaluableplayer.service.PlayerService;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

@Service
public class BasketballPlayerService extends PlayerService<BasketballPlayer> {

    public SportType getSportType() {
        return SportType.BASKETBALL;
    }

    protected BasketballPlayer createPlayerFromCSVRecord(CSVRecord record) {
        BasketballPlayer basketballPlayer = new BasketballPlayer();
        basketballPlayer.setName(record.get(0));
        basketballPlayer.setNickname(record.get(1));
        basketballPlayer.setNumber(Integer.parseInt(record.get(2)));
        basketballPlayer.setTeamName(record.get(3));
        basketballPlayer.setScoredPoints(Integer.parseInt(record.get(4)));
        basketballPlayer.setRebounds(Integer.parseInt(record.get(5)));
        basketballPlayer.setAssists(Integer.parseInt(record.get(6)));
        ratingPointsCount(basketballPlayer);
        pointsForTeamCount(basketballPlayer);
        return basketballPlayer;
    }

    protected void ratingPointsCount(BasketballPlayer basketballPlayer) {
        int ratingPoints = basketballPlayer.getScoredPoints() * 2 + basketballPlayer.getRebounds() + basketballPlayer.getAssists();
        basketballPlayer.setRatingPoints(ratingPoints);
    }

    protected void pointsForTeamCount(BasketballPlayer basketballPlayer) {
        basketballPlayer.setPointsForTeam(basketballPlayer.getScoredPoints());
    }
}
