package model.record;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.GameInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RecordsKeeper {
    private static final String RECORDS_PATH = "task3/src/main/resources/records.json";
    private static final String RECORDS_FILE_ERROR = "Ошибка работы с файлом с рекордами. ";
    private final List<Record> records;

    public RecordsKeeper() {
        this.records = new ArrayList<>(readRecords());
    }

    public void updateRecordIfBeaten(Record gameResult) {
        if (!isRecordBeaten(gameResult.gameInfo(), gameResult.timeSeconds())) {
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.records
                    .removeIf(record -> record.gameInfo().equals(gameResult.gameInfo()) &&
                            gameResult.timeSeconds() < record.timeSeconds());
            records.add(gameResult);
            objectMapper.writeValue(new File(RECORDS_PATH), records);
        } catch (IOException e) {
            System.err.println(RECORDS_FILE_ERROR + e);
        }
    }

    public boolean isRecordBeaten(GameInfo gameInfo, int timeSeconds) {
        return this.records.stream()
                .allMatch(record -> !record.gameInfo().equals(gameInfo) || timeSeconds < record.timeSeconds());
    }

    private List<Record> readRecords() {
        File recordsFile = new File(RECORDS_PATH);
        if(!recordsFile.exists()) {
            return Collections.emptyList();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return Arrays.asList(objectMapper.readValue(recordsFile, Record[].class));
        } catch (IOException e) {
            System.err.println(RECORDS_FILE_ERROR + e);
        }
        return Collections.emptyList();
    }

    public List<Record> getRecords() {
        return new ArrayList<>(this.records);
    }
}
