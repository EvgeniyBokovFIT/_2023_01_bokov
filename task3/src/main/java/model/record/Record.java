package model.record;

import model.GameInfo;

public record Record(
        GameInfo gameInfo,
        String username,
        int timeSeconds
) {
}
