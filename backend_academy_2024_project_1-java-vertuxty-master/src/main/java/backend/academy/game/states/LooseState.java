package backend.academy.game.states;

public class LooseState extends GameState {
    public LooseState() {
        super(true, false, UtilStateMessages.LOOSE_STATE);
    }
}
