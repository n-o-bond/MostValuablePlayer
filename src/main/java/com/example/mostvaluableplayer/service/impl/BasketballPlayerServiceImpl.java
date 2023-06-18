package com.example.mostvaluableplayer.service.impl;

import com.example.mostvaluableplayer.exception.CustomException;
import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.player.BasketballPlayer;
import com.example.mostvaluableplayer.service.PlayerService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("BasketballPlayerServiceImpl")
public class BasketballPlayerServiceImpl implements PlayerService<BasketballPlayer> {

    @Override
    public List<BasketballPlayer> parseUserDataFromCsv(MultipartFile file) {
        List<BasketballPlayer> players = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader());
            List<CSVRecord> csvRecords = parser.getRecords();

            for (CSVRecord record : csvRecords) {
                players.add(
                        playerWithCSVFile(
                                record.get(0), // name
                                record.get(1), // nickname
                                Integer.parseInt(record.get(2)), // number
                                record.get(3), // teamName
                                Integer.parseInt(record.get(4)), // scoredPoints
                                Integer.parseInt(record.get(5)), // rebounds
                                Integer.parseInt(record.get(6)) //assists
                        ));
            }

        } catch (IOException e) {
            throw new CustomException("Failed to parse CSV file", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return Collections.unmodifiableList(players);
    }

    private BasketballPlayer playerWithCSVFile(String name, String nickname, int number, String teamName, int scoredPoints, int rebounds, int assists) {
       BasketballPlayer basketballPlayer = new BasketballPlayer();
       basketballPlayer.setName(name);
       basketballPlayer.setNickname(nickname);
       basketballPlayer.setNumber(number);
       basketballPlayer.setTeamName(teamName);
       basketballPlayer.setScoredPoints(scoredPoints);
       basketballPlayer.setRebounds(rebounds);
       basketballPlayer.setAssists(assists);
       return basketballPlayer;
    }

    @Override
    public void ratingPointsCount(BasketballPlayer basketballPlayer) {
        int additionalRatingPoints = basketballPlayer.getScoredPoints() * 2 + basketballPlayer.getRebounds() + basketballPlayer.getAssists();
        basketballPlayer.setAdditionalRatingPoints(additionalRatingPoints);
    }

    @Override
    public void addRatingPointsForWinners(Game game) {
        game.getPlayers()
                .stream()
                .filter(player -> player.getTeamName().equals(game.getWinner().getName()))
                .forEach(player -> player.setAdditionalRatingPoints(10));
    }
}
