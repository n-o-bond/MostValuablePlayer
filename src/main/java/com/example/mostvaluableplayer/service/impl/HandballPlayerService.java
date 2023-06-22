package com.example.mostvaluableplayer.service.impl;

import com.example.mostvaluableplayer.model.SportType;
import com.example.mostvaluableplayer.model.player.HandballPlayer;
import com.example.mostvaluableplayer.service.PlayerService;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

@Service
public class HandballPlayerService extends PlayerService<HandballPlayer> {

    public SportType getSportType() {
        return SportType.HANDBALL;
    }

    protected HandballPlayer createPlayerFromCSVRecord(CSVRecord record) {
        HandballPlayer handballPlayer = new HandballPlayer();
        handballPlayer.setName(record.get(0));
        handballPlayer.setNickname(record.get(1));
        handballPlayer.setNumber(Integer.parseInt(record.get(2)));
        handballPlayer.setTeamName(record.get(3));
        handballPlayer.setGoalsMade(Integer.parseInt(record.get(4)));
        handballPlayer.setGoalsReceived(Integer.parseInt(record.get(5)));
        ratingPointsCount(handballPlayer);
        return handballPlayer;
    }

    protected void ratingPointsCount(HandballPlayer handballPlayer) {
        int additionalRatingPoints = handballPlayer.getGoalsMade() * 2 - handballPlayer.getGoalsReceived();
        handballPlayer.setRatingPoints(additionalRatingPoints);
    }
}