package backend.academy.game.states;

public class IncorrectState extends GameState {
    public IncorrectState() {
        super(false, false, UtilStateMessages.INCORRECT_STATE);
    }
}
