package backend.academy.game.states;

public class WinState extends GameState {
    public WinState() {
        super(true, false, UtilStateMessages.WIN_STATE);
    }
}
