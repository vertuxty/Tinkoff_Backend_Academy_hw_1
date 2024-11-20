package backend.academy.gallows;

import backend.academy.game.UtilGameConstants;
import backend.academy.game.Gallows;
import backend.academy.game.Game;
import backend.academy.game.HumanPlayer;
import backend.academy.game.Player;
import backend.academy.game.UtilMessages;
import backend.academy.game.dictionary.UtilDictionary;
import backend.academy.game.dictionary.Word;
import backend.academy.game.states.CorrectState;
import backend.academy.game.states.ErrorInitState;
import backend.academy.game.states.ErrorInputGuessState;
import backend.academy.game.states.GameState;
import backend.academy.game.states.HintRequestState;
import backend.academy.game.states.IncorrectState;
import backend.academy.game.states.LooseState;
import backend.academy.game.states.RepeatState;
import backend.academy.game.states.UtilStateMessages;
import backend.academy.game.states.WinState;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JsonConsoleTest {

    @Nested
    class TestConfig {
        final int pos = UtilGameConstants.MAX_ATTEMPTS - UtilGameConstants.DIFFICULTY - 1;
        final String start = UtilMessages.WELCOME_MESSAGE + "\n" + UtilMessages.CHOOSE_DIFFICULTY_MESSAGE + "\n";
        final String afterChooseDifficulty = UtilMessages.CHOOSE_GROUP_MESSAGE + "\n";
        ByteArrayOutputStream byteArrayOutputStream;

        @BeforeEach
        void init() {
            UtilDictionary.jsonPath = "src/test/java/backend/academy/gallows/resources/consoleTest.json";
            byteArrayOutputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(byteArrayOutputStream));

        }

        @Test
        void testChooseDifficultyIncorrect_1() {
            String input = "0";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            Game game = new Game();
            assertThatThrownBy(game::run);
            String expected = start + """
                1. EASY
                2. HARD
                """ + UtilMessages.ERROR_INPUT_DIFFICULTY + "\n" + UtilMessages.CHOOSE_DIFFICULTY_MESSAGE + "\n" + """
                1. EASY
                2. HARD
                """.trim();
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expected);
        }

        @Test
        void testChooseDifficultyIncorrect_2() {
            String input = "aaaa";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            Game game = new Game();
            assertThatThrownBy(game::run);
            String expected = start + """
                1. EASY
                2. HARD
                """ + UtilMessages.ERROR_INPUT_DIFFICULTY + "\n" + UtilMessages.CHOOSE_DIFFICULTY_MESSAGE + "\n" + """
                1. EASY
                2. HARD
                """.trim();
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expected);
        }

        @Test
        void testChooseDifficultyError() {
            UtilDictionary.jsonPath = "src/test/java/backend/academy/gallows/resources/emptyList.json";

            String input = "1\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            Game game = new Game();
            game.run();
            String expectedFirst = start + UtilMessages.ERROR + UtilMessages.ERROR_NO_WORDS_FOR_DIFFICULTY;
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expectedFirst.trim());
        }

        @Test
        void testChooseDifficultyCorrect1() {
            String input = "1\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            Game game = new Game();
            assertThatThrownBy(game::run);
            String expectedFirst = start + """
                1. EASY
                2. HARD
                You chose difficulty EASY""" + "\n" + UtilMessages.CHOOSE_GROUP_MESSAGE + "\n" + """
                1. ANIME
                2. FILMS
                """;
            String expectedSecond = start + """
                1. HARD
                2. EASY
                You chose difficulty HARD""" + "\n" + UtilMessages.CHOOSE_GROUP_MESSAGE + "\n" + """
                1. ANIME
                2. FILMS
                """;

            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isNotEqualTo(expectedSecond.trim());
            assertThat(output).isEqualTo(expectedFirst.trim());
        }

        @Test
        void testChooseGroupCorrect1() {
            UtilDictionary.jsonPath = "src/test/java/backend/academy/gallows/resources/groupConsoleTest.json";
            String input = "1\n1";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            Game game = new Game();
            assertThatThrownBy(game::run);
            String expected = start + """
                1. EASY
                2. HARD
                You chose difficulty EASY
                """ + afterChooseDifficulty + """
                1. ANIME
                You chose group ANIME
                """ + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is ANIME
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos] + "\n" + """
                Attempts Left: 7
                Word: _ _ _ _ _ _
                """.trim();
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expected);
        }

        @Test
        void testChooseGroupCorrect2() {
            UtilDictionary.jsonPath = "src/test/java/backend/academy/gallows/resources/groupConsoleTest.json";
            String input = "2\n1";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            Game game = new Game();
            assertThatThrownBy(game::run);
            String expected = start + """
                1. EASY
                2. HARD
                You chose difficulty HARD
                """ + afterChooseDifficulty + """
                1. FILMS
                You chose group FILMS
                """ + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is FILMS
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos] + "\n" + """
                Attempts Left: 7
                Word: _ _ _ _ _ _ _ _ _
                """.trim();
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expected);
        }

        @Test
        void testChooseGroupIncorrect1() {
            UtilDictionary.jsonPath = "src/test/java/backend/academy/gallows/resources/groupConsoleTest.json";
            String input = "1\n-1\n1";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            Game game = new Game();
            assertThatThrownBy(game::run);
            String expected = start + """
                1. EASY
                2. HARD
                You chose difficulty EASY
                """ + UtilMessages.CHOOSE_GROUP_MESSAGE + "\n" + """
                1. ANIME
                """ + UtilMessages.ERROR_INPUT_GROUP + "\n" + UtilMessages.CHOOSE_GROUP_MESSAGE + "\n" + """
                1. ANIME
                You chose group ANIME
                """ + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is ANIME
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos] + "\n" + """
                Attempts Left: 7
                Word: _ _ _ _ _ _""".trim();
            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(output).isEqualTo(expected);
        }

        @Test
        void testInputLetter() throws IOException {
            String input = """
                a
                b
                c
                a
                e
                f
                F
                d
                """;
            Player player =
                new HumanPlayer(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes()))));
            Gallows gallows = new Gallows(new Word("dada", "new Valve game"), 8);
            String inputLine = player.makeMove();
            assertThat(gallows.makeMove(inputLine)).isInstanceOf(CorrectState.class);
            inputLine = player.makeMove();
            assertThat(gallows.makeMove(inputLine)).isNotInstanceOf(CorrectState.class);
            inputLine = player.makeMove();
            assertThat(gallows.makeMove(inputLine)).isInstanceOf(IncorrectState.class);
            inputLine = player.makeMove();
            assertThat(gallows.makeMove(inputLine)).isInstanceOf(RepeatState.class);
            inputLine = player.makeMove();
            assertThat(gallows.makeMove(inputLine)).isNotInstanceOf(RepeatState.class);
            inputLine = player.makeMove();
            assertThat(gallows.makeMove(inputLine)).isInstanceOf(IncorrectState.class);
            inputLine = player.makeMove();
            assertThat(gallows.makeMove(inputLine)).isInstanceOf(RepeatState.class);
            inputLine = player.makeMove();
            assertThat(gallows.makeMove(inputLine)).isInstanceOf(WinState.class);
        }

        @Test
        void testCaseOfLetter() throws IOException {
            String input = """
                a
                A
                a
                b
                B
                C
                c
                """;
            Player player =
                new HumanPlayer(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes()))));
            Gallows gallows = new Gallows(new Word("dada", "new Valve game"), 8);

            String inputLine1 = player.makeMove();
            GameState state1 = gallows.makeMove(inputLine1);
            assertThat(state1).isInstanceOf(CorrectState.class);
            assertThat(state1.message()).isEqualTo(UtilStateMessages.CORRECT_STATE);
            assertThat(state1.terminal()).isFalse();
            assertThat(state1.error()).isFalse();

            String inputLine2 = player.makeMove();
            GameState state2 = gallows.makeMove(inputLine2);
            assertThat(state2).isInstanceOf(RepeatState.class);
            assertThat(state2.message()).isEqualTo(UtilStateMessages.REPEAT_STATE);
            assertThat(state2.terminal()).isFalse();
            assertThat(state2.error()).isFalse();

            String inputLine3 = player.makeMove();
            GameState state3 = gallows.makeMove(inputLine3);
            assertThat(state3).isInstanceOf(RepeatState.class);
            assertThat(state3.message()).isEqualTo(UtilStateMessages.REPEAT_STATE);
            assertThat(state3.terminal()).isFalse();
            assertThat(state3.error()).isFalse();

            assertThat(inputLine1).isEqualTo(inputLine2);
            assertThat(inputLine2).isEqualTo(inputLine3);

            String inputLine4 = player.makeMove();
            GameState state4 = gallows.makeMove(inputLine4);
            assertThat(state4).isInstanceOf(IncorrectState.class);
            assertThat(state4.message()).isEqualTo(UtilStateMessages.INCORRECT_STATE);
            assertThat(state4.terminal()).isFalse();
            assertThat(state4.error()).isFalse();

            String inputLine5 = player.makeMove();
            GameState state5 = gallows.makeMove(inputLine5);
            assertThat(state5).isInstanceOf(RepeatState.class);
            assertThat(state5.message()).isEqualTo(UtilStateMessages.REPEAT_STATE);
            assertThat(state5.terminal()).isFalse();
            assertThat(state5.error()).isFalse();

            assertThat(inputLine4).isEqualTo(inputLine5);
            assertThat(inputLine4).isNotEqualTo(inputLine3);

            String inputLine6 = player.makeMove();
            GameState state6 = gallows.makeMove(inputLine6);
            assertThat(state6).isInstanceOf(IncorrectState.class);
            assertThat(state6.message()).isEqualTo(UtilStateMessages.INCORRECT_STATE);
            assertThat(state6.terminal()).isFalse();
            assertThat(state6.error()).isFalse();

            String inputLine7 = player.makeMove();
            GameState state7 = gallows.makeMove(inputLine7);
            assertThat(state7).isInstanceOf(RepeatState.class);
            assertThat(state7.message()).isEqualTo(UtilStateMessages.REPEAT_STATE);
            assertThat(state7.terminal()).isFalse();
            assertThat(state7.error()).isFalse();

            assertThat(inputLine6).isEqualTo(inputLine7);
            assertThat(inputLine6).isNotEqualTo(inputLine4);
        }

        @Test
        void testHintResult() throws IOException {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            System.setOut(new PrintStream(byteArrayOutputStream));
            String input = """
                a
                hiNt
                hint
                Hint
                HInt
                HINT
                Hit
                """;
            Player player =
                new HumanPlayer(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes()))));
            Gallows gallows = new Gallows(new Word("drift", "this do every body in NFS"), 5);

            String inputLine = player.makeMove();
            GameState moveResult = gallows.makeMove(inputLine);
            assertThat(moveResult).isInstanceOf(IncorrectState.class);
            assertThat(moveResult.terminal()).isFalse();
            assertThat(moveResult.message()).isEqualTo(UtilStateMessages.INCORRECT_STATE);

            assertThat(gallows.showHint()).isEqualTo(false);
            byteArrayOutputStream.reset();

            inputLine = player.makeMove();
            GameState moveResult1 = gallows.makeMove(inputLine);
            assertThat(moveResult1).isInstanceOf(HintRequestState.class);
            assertThat(moveResult1.terminal()).isFalse();
            assertThat(moveResult1.message()).isEqualTo(UtilStateMessages.HINT_REQUEST_STATE);

            assertThat(gallows.showHint()).isEqualTo(true);

            inputLine = player.makeMove();
            GameState moveResult2 = gallows.makeMove(inputLine);
            assertThat(moveResult2).isInstanceOf(HintRequestState.class);
            assertThat(moveResult2.terminal()).isFalse();
            assertThat(moveResult2.message()).isEqualTo(UtilStateMessages.HINT_REQUEST_STATE);

            assertThat(gallows.showHint()).isEqualTo(true);

            inputLine = player.makeMove();
            GameState moveResult3 = gallows.makeMove(inputLine);
            assertThat(moveResult3).isInstanceOf(HintRequestState.class);
            assertThat(moveResult3.terminal()).isFalse();
            assertThat(moveResult3.message()).isEqualTo(UtilStateMessages.HINT_REQUEST_STATE);

            assertThat(gallows.showHint()).isEqualTo(true);

            inputLine = player.makeMove();
            GameState moveResult4 = gallows.makeMove(inputLine);
            assertThat(moveResult4).isInstanceOf(HintRequestState.class);
            assertThat(moveResult4.terminal()).isFalse();
            assertThat(moveResult4.message()).isEqualTo(UtilStateMessages.HINT_REQUEST_STATE);

            assertThat(gallows.showHint()).isEqualTo(true);

            inputLine = player.makeMove();
            GameState moveResult5 = gallows.makeMove(inputLine);
            assertThat(moveResult5).isInstanceOf(HintRequestState.class);
            assertThat(moveResult5.terminal()).isFalse();
            assertThat(moveResult5.message()).isEqualTo(UtilStateMessages.HINT_REQUEST_STATE);

            assertThat(gallows.showHint()).isEqualTo(true);

            GameState state = gallows.makeMove(player.makeMove());
            assertThatThrownBy(() -> gallows.makeMove(player.makeMove()));
            assertThat(gallows.showHint()).isEqualTo(true);
            assertThat(state).isInstanceOf(ErrorInputGuessState.class);
            assertThat(state.message()).isEqualTo(UtilStateMessages.ERROR_INPUT_GUESS_STATE);
        }

        @Test
        void testInitializationError() {
            UtilDictionary.jsonPath = "src/test/java/backend/academy/gallows/resources/lengthWordTest.json";

            String input = "1\n2\n";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            Game game = new Game();
            AtomicReference<GameState> gameState = new AtomicReference<>();
            assertThatCode(() -> gameState.set(game.run())).doesNotThrowAnyException();
            assertThat(gameState.get()).isInstanceOf(ErrorInitState.class);
            assertThat(gameState.get().message()).isEqualTo(UtilStateMessages.ERROR_INIT_STATE);
        }

        @Test
        void testHintInGame() {
            UtilDictionary.jsonPath = "src/test/java/backend/academy/gallows/resources/groupConsoleTest.json";
            String input = "1\n1\ne\nHINT";
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            Game game = new Game();
            assertThatThrownBy(game::run);
            String expected = start + """
                1. EASY
                2. HARD
                You chose difficulty EASY
                """ + UtilMessages.CHOOSE_GROUP_MESSAGE + "\n" + """
                1. ANIME
                You chose group ANIME
                """ + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is ANIME
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos] + "\n" + """
                Attempts Left: 7
                Word: _ _ _ _ _ _
                You print: E
                """ + UtilStateMessages.INCORRECT_STATE + "\n" + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is ANIME
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos + 1] + "\n" + """
                Attempts Left: 6
                Word: _ _ _ _ _ _
                You print: HINT
                """ + UtilStateMessages.HINT_REQUEST_STATE + "\n" + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is ANIME
                HINT: uzumaki
                """ + UtilMessages.HANGMAN_STAGES[pos + 1] + "\n" + """
                Attempts Left: 6
                Word: _ _ _ _ _ _
                """.trim();
            String realOutput = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(realOutput).isEqualTo(expected);
        }

        @Test
        void runConsoleGameWinTest() {
            UtilDictionary.jsonPath = "src/test/java/backend/academy/gallows/resources/terminalTest.json";

            String input = """
                1
                1
                d
                a
                """;
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            Game game = new Game();
            GameState moveResult = game.run();
            String expectedOut = start + """
                1. EASY
                You chose difficulty EASY
                """ + UtilMessages.CHOOSE_GROUP_MESSAGE + "\n" + """
                1. ANIME
                You chose group ANIME
                """ + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is ANIME
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos] + "\n" + """
                Attempts Left: 7
                Word: _ _ _ _
                You print: D
                """ + UtilStateMessages.CORRECT_STATE + "\n" + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is ANIME
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos] + "\n" + """
                Attempts Left: 7
                Word: D _ D _
                You print: A
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos] + "\n" + """
                Attempts Left: 7
                Word: D A D A
                You guessed the word, congratulations!
                Answer is DADA
                """ + UtilMessages.END_MESSAGE;
            String realOutput = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();

            assertThat(moveResult).isInstanceOf(WinState.class);
            assertThat(moveResult.message()).isEqualTo(UtilStateMessages.WIN_STATE);
            assertThat(realOutput.length()).isEqualTo(expectedOut.trim().length());
            assertThat(realOutput).isEqualTo(expectedOut.trim());
        }

        @Test
        void runConsoleGameLooseTest() {
            UtilDictionary.jsonPath = "src/test/java/backend/academy/gallows/resources/terminalTest.json";
            String input = """
                1
                1
                a
                b
                c
                e
                k
                l
                i
                o
                """;
            System.setIn(new ByteArrayInputStream(input.getBytes()));
            Game game = new Game();
            GameState moveResult = game.run();
            String expectedOut = start + """
                1. EASY
                You chose difficulty EASY
                """ + UtilMessages.CHOOSE_GROUP_MESSAGE + "\n" + """
                1. ANIME
                You chose group ANIME
                """ + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is ANIME
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos] + "\n" + """
                Attempts Left: 7
                Word: _ _ _ _
                You print: A
                """ + UtilStateMessages.CORRECT_STATE + "\n" + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is ANIME
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos] + "\n" + """
                Attempts Left: 7
                Word: _ A _ A
                You print: B
                """ + UtilStateMessages.INCORRECT_STATE + "\n" + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is ANIME
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos + 1] + "\n" + """
                Attempts Left: 6
                Word: _ A _ A
                You print: C
                """ + UtilStateMessages.INCORRECT_STATE + "\n" + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is ANIME
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos + 2] + "\n" + """
                Attempts Left: 5
                Word: _ A _ A
                You print: E
                """ + UtilStateMessages.INCORRECT_STATE + "\n" + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is ANIME
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos + 3] + "\n" + """
                Attempts Left: 4
                Word: _ A _ A
                You print: K
                """ + UtilStateMessages.INCORRECT_STATE + "\n" + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is ANIME
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos + 4] + "\n" + """
                Attempts Left: 3
                Word: _ A _ A
                You print: L
                """ + UtilStateMessages.INCORRECT_STATE + "\n" + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is ANIME
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos + 5] + "\n" + """
                Attempts Left: 2
                Word: _ A _ A
                You print: I
                """ + UtilStateMessages.INCORRECT_STATE + "\n" + UtilMessages.NEXT_MOVE + "\n" + """
                Word category is ANIME
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos + 6] + "\n" + """
                Attempts Left: 1
                Word: _ A _ A
                You print: O
                """ + UtilMessages.BASE_HINT_MESSAGE + "\n" + UtilMessages.HANGMAN_STAGES[pos + 7] + "\n" + """
                Attempts Left: 0
                Word: _ A _ A
                """ + UtilStateMessages.LOOSE_STATE + "\n" + """
                Answer is DADA
                """ + UtilMessages.END_MESSAGE;
            String realOutput = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
            assertThat(moveResult).isInstanceOf(LooseState.class);
            assertThat(realOutput.length()).isEqualTo(expectedOut.trim().length());
            assertThat(realOutput).isEqualTo(expectedOut.trim());
        }
    }
}
