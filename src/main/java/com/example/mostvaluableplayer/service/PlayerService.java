package com.example.mostvaluableplayer.service;

import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.player.BasketballPlayer;
import com.example.mostvaluableplayer.model.player.Player;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlayerService<T extends Player> {

    List<T> parseUserDataFromCsv(MultipartFile file);

    void ratingPointsCount(T player);

    void addRatingPointsForWinners(Game game);
}
