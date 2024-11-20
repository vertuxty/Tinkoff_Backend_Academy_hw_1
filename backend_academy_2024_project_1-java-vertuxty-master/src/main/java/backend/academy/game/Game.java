package backend.academy.game;

import backend.academy.game.dictionary.JsonDictionary;
import backend.academy.game.dictionary.Word;
import backend.academy.game.dictionary.WordDifficulty;
import backend.academy.game.states.ErrorInitState;
import backend.academy.game.states.GameState;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import lombok.SneakyThrows;

@SuppressWarnings("RegexpSinglelineJava")
public class Game {

    private final Player player;
    private final JsonDictionary jsonDictionary;
    private String group;
    private Word word;

    public Game() {
        this.jsonDictionary = new JsonDictionary();
        this.player = new HumanPlayer(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)));
    }

    @SneakyThrows
    public void init() {
        jsonDictionary.load();
        printMessage(UtilMessages.WELCOME_MESSAGE);
        WordDifficulty wordDifficulty = getDifficultyJson();
        group = getGroupByDifficulty(wordDifficulty);
        word = jsonDictionary.getWordByGroupAndDifficulty(group, wordDifficulty);
    }

    @SneakyThrows
    public GameState run() { // запускает игру
        try {
            init();
        } catch (IllegalArgumentException e) {
            printMessage(UtilMessages.ERROR + e.getMessage());
            player.close();
            return new ErrorInitState(); // maybe create error state
        }
        Gallows gallows = new Gallows(word, UtilGameConstants.DIFFICULTY); // game logic
        while (true) {

            printMessage(UtilMessages.NEXT_MOVE);
            printMessage("Word category is " + group);
            draw(gallows, word);

            String input = player.makeMove(); // чтение ввода.
            GameState state = gallows.makeMove(input);
            // Если ошибочное состояние, то просто уходим на повтор. Состояние игры (угаданные буквы и тд не изменилось)
            if (state.error()) {
                printMessage(state.message());
                continue;
            }
            printMessage("You print: " + input);

            if (state.terminal()) {
                draw(gallows, word);
                printMessage(state.message());
                printMessage("Answer is " + word.word().toUpperCase());
                printMessage(UtilMessages.END_MESSAGE);
                player.close();
                return state;
            }
            printMessage(state.message());
        }
    }

    @SneakyThrows
    private WordDifficulty getDifficultyJson() {
        WordDifficulty wordDifficulty;
        while (true) {
            printMessage(UtilMessages.CHOOSE_DIFFICULTY_MESSAGE);
            if (jsonDictionary.isEmpty()) {
                throw new IllegalArgumentException(UtilMessages.ERROR_NO_WORDS_FOR_DIFFICULTY);
            }
            printArguments(jsonDictionary.difficulties());
            String difficulty = player.makeMove(); // input form player
            try {
                wordDifficulty = jsonDictionary.getDifficulty(difficulty); // get from dictionary
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                printMessage(UtilMessages.ERROR_INPUT_DIFFICULTY);
                continue;
            }
            if (jsonDictionary.getGroupsByDifficulty(wordDifficulty).isEmpty()) {
                throw new IllegalArgumentException(UtilMessages.ERROR_NO_WORDS_FOR_DIFFICULTY);
            }
            printMessage("You chose difficulty " + wordDifficulty);
            return wordDifficulty;
        }
    }

    private String getGroupByDifficulty(WordDifficulty wordDifficulty) throws IOException {
        while (true) {
            printMessage(UtilMessages.CHOOSE_GROUP_MESSAGE);
            printArguments(jsonDictionary.getGroupsByDifficulty(wordDifficulty));
            String input = player.makeMove(); // input from player
            try {
                group = jsonDictionary.getGroup(input, wordDifficulty); // get from dictionary
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                printMessage(UtilMessages.ERROR_INPUT_GROUP);
                continue;
            }
            printMessage("You chose group " + group);
            return group;
        }
    }

    // draw hangman.
    private void draw(Gallows gallows, Word word) {
        if (gallows.showHint()) {
            printMessage("HINT: " + word.hint());
        } else {
            printMessage(UtilMessages.BASE_HINT_MESSAGE);
        }
        printMessage(UtilMessages.HANGMAN_STAGES[UtilGameConstants.INITIAL_STATE + gallows.wrongGuess() - 1]);
        printMessage("Attempts Left: " + (UtilGameConstants.DIFFICULTY - gallows.wrongGuess()));
        printMessage("Word: " + String.join(" ", gallows.getWordState()));
    }

    // i don't like how system.out.println looks...
    private void printMessage(String msg) {
        System.out.println(msg);
    }

    // prints difficulties and groups
    private <T> void printArguments(Collection<T> collection) {
        Iterator<T> iterator = collection.iterator();
        int pos = 1;
        while (iterator.hasNext()) {
            printMessage(pos + ". " + iterator.next());
            ++pos;
        }
    }
}
