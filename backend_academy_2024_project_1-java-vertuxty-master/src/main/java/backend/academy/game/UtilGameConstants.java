package backend.academy.game;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UtilGameConstants {
    public static final int MAX_ATTEMPTS = 10;
    public static final int DIFFICULTY = 7;
    public static final int INITIAL_STATE = MAX_ATTEMPTS - DIFFICULTY;
}
