package com.example.mostvaluableplayer.service;

import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.Tournament;
import com.example.mostvaluableplayer.model.player.Player;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TournamentService {

    Tournament createTournament(List<Game> games);

    Player determineMostValuablePlayer(List<? extends Player> players);

    Tournament getTournamentFromFiles(List<MultipartFile> files);
}
