package backend.academy.game;

import backend.academy.game.dictionary.Word;
import backend.academy.game.states.CorrectState;
import backend.academy.game.states.ErrorInputGuessState;
import backend.academy.game.states.GameState;
import backend.academy.game.states.HintRequestState;
import backend.academy.game.states.IncorrectState;
import backend.academy.game.states.LooseState;
import backend.academy.game.states.RepeatState;
import backend.academy.game.states.WinState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import lombok.Getter;

public class Gallows {
    public static final String HINT = "HINT";
    private final String[] word;
    private final HashMap<String, List<Integer>> wordMap;
    private final HashSet<String> usedLetters;
    private int correctGuess;
    @Getter private boolean showHint = false;
    @Getter private int wrongGuess;
    private final int difficultyAttempts;

    // init board.
    public Gallows(final Word word, final int difficultyAttempts) {
        this.correctGuess = 0;
        this.wrongGuess = 0;
        this.difficultyAttempts = difficultyAttempts;

        this.wordMap = new HashMap<>();
        this.word = new String[word.word().length()];
        this.usedLetters = new HashSet<>();

        init(word.word());
    }

    // Инициализация логики (заполнение слова "_", сохранение в мапу символов)
    private void init(final String word) {
        Arrays.fill(this.word, "_");
        for (int i = 0; i < word.length(); i++) {
            final String ch = String.valueOf(word.charAt(i)).toUpperCase();
            if (!wordMap.containsKey(ch)) {
                wordMap.put(ch, new ArrayList<>());
            }
            wordMap.get(ch).add(i);
        }
    }

    public String[] getWordState() {
        return word;
    }

    @SuppressWarnings("ReturnCount")
    public GameState makeMove(final String letter) {
        if (wrongGuess >= difficultyAttempts) {
            return new LooseState();
        }
        String upperCase = letter.toUpperCase();
        if (HINT.equals(letter)) {
            showHint = true;
            return new HintRequestState();
        }
        if (letter.length() != 1) {
            return new ErrorInputGuessState();
        }
        if (usedLetters.contains(upperCase)) {
            return new RepeatState();
        }
        usedLetters.add(upperCase);

        if (!wordMap.containsKey(upperCase)) {
            return getStateIncorrectGuess();
        }
        return putLetter(upperCase);
    }

    private GameState putLetter(String letter) {
        correctGuess += wordMap.get(letter).size();
        for (Integer i : wordMap.get(letter)) {
            word[i] = letter;
        }
        return correctGuess == word.length ? new WinState() : new CorrectState();
    }

    private GameState getStateIncorrectGuess() {
        ++wrongGuess;
        if (wrongGuess == difficultyAttempts) {
            return new LooseState();
        }
        return new IncorrectState();
    }
}
