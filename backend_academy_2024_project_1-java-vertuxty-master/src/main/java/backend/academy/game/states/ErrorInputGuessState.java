package backend.academy.game.states;

public class ErrorInputGuessState extends GameState {
    public ErrorInputGuessState() {
        super(false, true, UtilStateMessages.ERROR_INPUT_GUESS_STATE);
    }
}
