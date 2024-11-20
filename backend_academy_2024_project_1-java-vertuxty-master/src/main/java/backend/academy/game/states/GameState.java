package backend.academy.game.states;

import lombok.Getter;

@Getter public abstract class GameState {
    private final boolean terminal;
    private final boolean error;
    private final String message;

    protected GameState(boolean terminal, boolean error, String message) {
        this.terminal = terminal;
        this.message = message;
        this.error = error;
    }
}
