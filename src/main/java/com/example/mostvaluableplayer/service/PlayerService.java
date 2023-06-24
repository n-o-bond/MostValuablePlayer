package com.example.mostvaluableplayer.service;

import com.example.mostvaluableplayer.exception.FailedParsingFileException;
import com.example.mostvaluableplayer.model.SportType;
import com.example.mostvaluableplayer.model.player.Player;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class PlayerService<T extends Player> {

    protected abstract T createPlayerFromCSVRecord(CSVRecord record);

    protected abstract void ratingPointsCount(T player);

    protected abstract void pointsForTeamCount(T player);

    public abstract SportType getSportType();

    public List<T> parseUserDataFromCsv(MultipartFile file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.withDelimiter(';').withFirstRecordAsHeader());
            List<CSVRecord> csvRecords = parser.getRecords();
            List<T> players = new ArrayList<>();
            for (CSVRecord record : csvRecords) {
                try {
                    players.add(createPlayerFromCSVRecord(record));
                } catch (NumberFormatException e) {
                    throw new FailedParsingFileException("Failed to parse CSV file: Invalid number format in the CSV record");
                } catch (ArrayIndexOutOfBoundsException e) {
                    throw new FailedParsingFileException("Failed to parse CSV file: Missing fields in the CSV record");
                }
            }
            return Collections.unmodifiableList(players);
        } catch (IllegalArgumentException e) {
            throw new FailedParsingFileException("Failed to parse CSV file: Illegal CSV record");
        } catch (IOException e) {
            throw new FailedParsingFileException("Failed to access CSV file", e);
        }
    }
}