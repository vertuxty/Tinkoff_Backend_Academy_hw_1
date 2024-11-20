package backend.academy.game;

import java.io.IOException;

public interface Player {
    String makeMove() throws IOException;

    void close() throws IOException;
}
