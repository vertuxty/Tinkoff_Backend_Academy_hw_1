package backend.academy.game;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("RegexpSinglelineJava")
public final class Main {

    public static void main(String[] args) {
        Game game = new Game();
        System.out.println(game.run());
    }
}
