package backend.academy.game;

import java.io.BufferedReader;
import lombok.SneakyThrows;

@SuppressWarnings("RegexpSinglelineJava")
public class HumanPlayer implements Player {
    private final BufferedReader bufferedReader;

    public HumanPlayer(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    @SneakyThrows
    public String makeMove() {
        String in = bufferedReader.readLine();
        String letter;
        if (in != null) {
            letter = in.toUpperCase();
        } else {
            throw new NullPointerException("No input");
        }
        return letter;
    }

    @SneakyThrows
    @Override
    public void close() {
        bufferedReader.close();
    }
}
