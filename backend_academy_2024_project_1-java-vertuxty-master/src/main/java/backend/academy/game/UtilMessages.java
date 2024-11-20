package backend.academy.game;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class UtilMessages {
    public static final String ERROR = "Error. ";
    public static final String NEXT_MOVE = "Guess next letter.";
    public static final String WELCOME_MESSAGE = "Welcome! This is HangMan Game.";
    public static final String CHOOSE_GROUP_MESSAGE =
        "Choose the group of words (print position of group, empty for random): ";
    public static final String END_MESSAGE = "Game is over! Bye!";
    public static final String CHOOSE_DIFFICULTY_MESSAGE =
        "Choose the difficulty of words (print position of group, empty for random): ";
    public static final String BASE_HINT_MESSAGE = "Print \"HINT\" in any register to get word's hint.";
    public static final String ERROR_NO_WORDS_FOR_DIFFICULTY = ERROR + "No words for this difficulty. Quit the game.";
    public static final String ERROR_INPUT_DIFFICULTY =
        ERROR + "Print difficulty's line number, other input is not allowed.";
    public static final String ERROR_INPUT_GROUP = ERROR + "Print group's line number, other input is not allowed.";

    public static final String[] HANGMAN_STAGES = new String[] {"""
        -----
        |
        |
        |
        |
        |
        --------
        """, """
        -----
        |   |
        |
        |
        |
        |
        --------
        """, """
        -----
        |   |
        |   O
        |
        |
        |
        --------
        """, """
        -----
        |   |
        |   O
        |   |
        |
        |
        --------
        """, """
        -----
        |   |
        |   O
        |  /|
        |
        |
        --------
        """, """
        -----
        |   |
        |   O
        |  /|\\
        |
        |
        --------
        """, """
        -----
        |   |
        |   O
        |  /|\\
        |  /
        |
        --------
        """, """
        -----
        |   |
        |   O
        |  /|\\
        |  / \\
        |
        --------
        """, """
        -----
        |   |
        |   O
        |  /|\\\\
        |  / \\
        |
        --------
        """, """
        -----
        |   |
        |   O
        |  /|\\\\
        |  / \\\\
        |
        --------
        """};
}
