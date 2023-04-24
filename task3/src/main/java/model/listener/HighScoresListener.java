package model.listener;

import model.record.Record;

import java.util.List;

public interface HighScoresListener {
    void onHighScoresUpdate(List<Record> records);
}
