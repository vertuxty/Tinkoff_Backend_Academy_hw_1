package backend.academy.game.states;

import lombok.experimental.UtilityClass;

@UtilityClass
public class UtilStateMessages {
    public static final String CORRECT_STATE = "Correct guess!";
    public static final String INCORRECT_STATE = "Incorrect guess!";
    public static final String WIN_STATE = "You guessed the word, congratulations!";
    public static final String LOOSE_STATE = "Unfortunate, you not guessed the word!";
    public static final String REPEAT_STATE = "This letter was already used, try again.";
    public static final String HINT_REQUEST_STATE = "Now hint is enabled!";
    public static final String ERROR_INPUT_GUESS_STATE = "Wrong input format, only ONE letter. Try again!";
    public static final String ERROR_INIT_STATE = "Initialization error! Quit game.";
}
