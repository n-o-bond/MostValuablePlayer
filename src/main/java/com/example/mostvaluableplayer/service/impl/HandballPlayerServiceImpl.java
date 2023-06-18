package com.example.mostvaluableplayer.service.impl;

import com.example.mostvaluableplayer.exception.CustomException;
import com.example.mostvaluableplayer.model.Game;
import com.example.mostvaluableplayer.model.player.HandballPlayer;
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

@Service("HandballPlayerServiceImpl")
public class HandballPlayerServiceImpl implements PlayerService<HandballPlayer> {
    @Override
    public List<HandballPlayer> parseUserDataFromCsv(MultipartFile file) {
        List<HandballPlayer> players = new ArrayList<>();
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
                                Integer.parseInt(record.get(4)), // goals made
                                Integer.parseInt(record.get(5)) // goals received
                        ));
            }

        } catch (IOException e) {
            throw new CustomException("Failed to parse CSV file", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        return Collections.unmodifiableList(players);
    }

    private HandballPlayer playerWithCSVFile(String name, String nickname, int number, String teamName, int goalsMade, int goalsReceived) {
    HandballPlayer handballPlayer = new HandballPlayer();
    handballPlayer.setName(name);
    handballPlayer.setNickname(nickname);
    handballPlayer.setNumber(number);
    handballPlayer.setTeamName(teamName);
    handballPlayer.setGoalsMade(goalsMade);
    handballPlayer.setGoalsReceived(goalsReceived);
    return handballPlayer;
    }

    @Override
    public void ratingPointsCount(HandballPlayer handballPlayer) {
        int additionalRatingPoints = handballPlayer.getGoalsMade() * 2 - handballPlayer.getGoalsReceived();
        handballPlayer.setAdditionalRatingPoints(additionalRatingPoints);
    }

    @Override
    public void addRatingPointsForWinners(Game game) {
        game.getPlayers()
                .stream()
                .filter(player -> player.getTeamName().equals(game.getWinner().getName()))
                .forEach(player -> player.setAdditionalRatingPoints(10));
    }
}
