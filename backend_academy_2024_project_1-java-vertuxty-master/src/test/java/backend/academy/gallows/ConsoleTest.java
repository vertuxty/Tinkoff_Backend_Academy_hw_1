//package backend.academy.gallows;
//
//import backend.academy.game.AppConfig;
//import backend.academy.game.Game;
//import backend.academy.game.GameInterface;
//import backend.academy.game.Player;
//import backend.academy.game.PlayerInterface;
//import backend.academy.game.dictionary.Word;
//import backend.academy.game.dictionary.WordCategory;
//import backend.academy.game.logic.Gallows;
//import backend.academy.game.states.CorrectState;
//import backend.academy.game.states.GameState;
//import backend.academy.game.states.HintRequestState;
//import backend.academy.game.states.IncorrectState;
//import backend.academy.game.states.LooseState;
//import backend.academy.game.states.RepeatState;
//import backend.academy.game.states.WinState;
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintStream;
//import java.util.List;
//import java.util.Map;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//
//public class ConsoleTest {
//
//    @Nested class TestConfig {
//        final String start = GameInterface.WELCOME_MSG + "\n" + GameInterface.DIFFICULTY_MSG + "\n";
//        final String afterChooseDifficulty = GameInterface.GROUP_MSG + "\n" + GameInterface.NEXT_MOVE + "\n";
//        ByteArrayOutputStream byteArrayOutputStream;
//
//        @BeforeEach void init() {
//            AppConfig.dictionary = Map.of(WordCategory.GAMES, List.of(new Word("dada", "new Valve game")));
//            byteArrayOutputStream = new ByteArrayOutputStream();
//            System.setOut(new PrintStream(byteArrayOutputStream));
//
//        }
//
//        @Test void testChooseGroupIncorrect_1() throws IOException {
//            AppConfig.dictionary =
//                Map.of(WordCategory.GAMES, List.of(new Word("dada", "new Valve game")), WordCategory.ANIME,
//                    List.of(new Word("naruto", "boruto")));
//
//            String input = "0";
//            System.setIn(new ByteArrayInputStream(input.getBytes()));
//            Game game = new Game();
//            assertThatThrownBy(game::run);
//            String expected = start + """
//                1. ANIME
//                2. GAMES
//                """ + GameInterface.ERROR + GameInterface.NO_SUCH_GROUP_WITH_THIS_NUM_TRY_AGAIN + "\n" +
//                GameInterface.GROUP_MSG + "\n" + """
//                1. ANIME
//                2. GAMES
//                """.trim();
//            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
//            assertThat(output).isEqualTo(expected);
//        }
//
//        @Test void testChooseGroupIncorrect_2() throws IOException {
//            AppConfig.dictionary =
//                Map.of(WordCategory.GAMES, List.of(new Word("dada", "new Valve game")), WordCategory.ANIME,
//                    List.of(new Word("naruto", "boruto")));
//
//            String input = "aaaa";
//            System.setIn(new ByteArrayInputStream(input.getBytes()));
//            Game game = new Game();
//            assertThatThrownBy(game::run);
//            String expected = start + """
//                1. ANIME
//                2. GAMES
//                """ + GameInterface.ERROR + GameInterface.WRONG_NUMBER_FORMAT_TRY_AGAIN + "\n" +
//                GameInterface.GROUP_MSG + "\n" + """
//                1. ANIME
//                2. GAMES
//                """.trim();
//            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
//            assertThat(output).isEqualTo(expected);
//        }
//
//        @Test void testChooseGroupCorrect() throws IOException {
//
//            AppConfig.dictionary =
//                Map.of(WordCategory.GAMES, List.of(new Word("dada", "new Valve game")), WordCategory.ANIME,
//                    List.of(new Word("naruto", "boruto")));
//            String input = "1\n";
//            System.setIn(new ByteArrayInputStream(input.getBytes()));
//            Game game = new Game();
//            assertThatThrownBy(game::run);
//            String expectedFirst = start + """
//                1. GAMES
//                2. ANIME
//                You chose GAMES""" + "\n" + GameInterface.DIFFICULTY_MSG;
//            String expectedSecond = start + """
//                1. ANIME
//                2. GAMES
//                You chose ANIME""" + "\n" + GameInterface.DIFFICULTY_MSG;
//
//            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
//            assertThat(output).isEqualTo(expectedSecond.trim());
//            assertThat(output).isNotEqualTo(expectedFirst.trim());
//        }
//
//        @Test void testChooseDifficultyCorrect() throws IOException {
//            String difficulty = "7";
//            String input = "1\n" + difficulty;
//            System.setIn(new ByteArrayInputStream(input.getBytes()));
//            Game game = new Game();
//            assertThatThrownBy(game::run);
//            String expected = start + """
//                1. GAMES
//                You chose GAMES
//                """ + afterChooseDifficulty + """
//                Word category is GAMES
//                """ + GameInterface.BASE_HINT_MSG + "\n" + """
//                -----
//                |   |
//                |   O
//                |
//                |
//                |
//                --------
//
//                Attempts Left: 7
//                Word: _ _ _ _
//                """.trim();
//            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
//            assertThat(output).isEqualTo(expected);
//        }
//
//        @Test void testChooseDifficultyWrong() throws IOException {
//            String difficultyIncorrect = "10";
//            String difficultyCorrect = "7";
//            String input = "1\n" + difficultyIncorrect + "\n" + difficultyCorrect;
//            System.setIn(new ByteArrayInputStream(input.getBytes()));
//            Game game = new Game();
//            assertThatThrownBy(game::run);
//            String expected = start + """
//                1. GAMES
//                You chose GAMES
//                """ + GameInterface.DIFFICULTY_MSG + "\n" + GameInterface.ERROR +
//                GameInterface.DIFFICULTY_MSG_ERROR_INTERVAL + "\n" + GameInterface.DIFFICULTY_MSG + "\n" +
//                GameInterface.NEXT_MOVE + "\n" + """
//                Word category is GAMES
//                """ + GameInterface.BASE_HINT_MSG + "\n" + """
//                -----
//                |   |
//                |   O
//                |
//                |
//                |
//                --------
//
//                Attempts Left: 7
//                Word: _ _ _ _""".trim();
//            String output = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
//            assertThat(output).isEqualTo(expected);
//        }
//
//        @Test void testInputLetter() throws IOException {
//            String input = """
//                a
//                b
//                c
//                a
//                e
//                f
//                F
//                d
//                """;
//            Player player = new PlayerInterface(
//                new BufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes()))));
//            Gallows gallows = new Gallows(new Word("dada", "new Valve game"), 8);
//            assertThat(player.makeMove(gallows)).isInstanceOf(CorrectState.class);
//            assertThat(player.makeMove(gallows)).isNotInstanceOf(CorrectState.class);
//            assertThat(player.makeMove(gallows)).isInstanceOf(IncorrectState.class);
//            assertThat(player.makeMove(gallows)).isInstanceOf(RepeatState.class);
//            assertThat(player.makeMove(gallows)).isNotInstanceOf(RepeatState.class);
//            assertThat(player.makeMove(gallows)).isInstanceOf(IncorrectState.class);
//            assertThat(player.makeMove(gallows)).isInstanceOf(RepeatState.class);
//            assertThat(player.makeMove(gallows)).isInstanceOf(WinState.class);
//        }
//
//        @Test void testCaseOfLetter() throws IOException {
//            String input = """
//                a
//                A
//                a
//                b
//                B
//                C
//                c
//                """;
//            Player player = new PlayerInterface(
//                new BufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes()))));
//            Gallows gallows = new Gallows(new Word("dada", "new Valve game"), 8);
//            assertThat(player.makeMove(gallows)).isInstanceOf(CorrectState.class);
//            String expected = """
//                You print letter: A
//                """.trim();
//            assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim()).isEqualTo(
//                expected);
//            byteArrayOutputStream.reset();
//
//            assertThat(player.makeMove(gallows)).isInstanceOf(RepeatState.class);
//            expected = """
//                You print letter: A
//                """.trim();
//            assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim()).isEqualTo(
//                expected);
//            byteArrayOutputStream.reset();
//
//            assertThat(player.makeMove(gallows)).isInstanceOf(RepeatState.class);
//            expected = """
//                You print letter: A
//                """.trim();
//            assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim()).isEqualTo(
//                expected);
//            byteArrayOutputStream.reset();
//
//            assertThat(player.makeMove(gallows)).isInstanceOf(IncorrectState.class);
//            expected = """
//                You print letter: B
//                """.trim();
//            assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim()).isEqualTo(
//                expected);
//            byteArrayOutputStream.reset();
//
//            assertThat(player.makeMove(gallows)).isInstanceOf(RepeatState.class);
//            expected = """
//                You print letter: B
//                """.trim();
//            assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim()).isEqualTo(
//                expected);
//            byteArrayOutputStream.reset();
//
//            assertThat(player.makeMove(gallows)).isInstanceOf(IncorrectState.class);
//            expected = """
//                You print letter: C
//                """.trim();
//            assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim()).isEqualTo(
//                expected);
//            byteArrayOutputStream.reset();
//
//            assertThat(player.makeMove(gallows)).isInstanceOf(RepeatState.class);
//            expected = """
//                You print letter: C
//                """.trim();
//            assertThat(byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim()).isEqualTo(
//                expected);
//            byteArrayOutputStream.reset();
//
//        }
//
//        @Test void testHintResult() throws IOException {
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            System.setOut(new PrintStream(byteArrayOutputStream));
//            String input = """
//                a
//                hiNt
//                hint
//                Hint
//                HInt
//                HINT
//                Hit
//                """;
//            Player player = new PlayerInterface(
//                new BufferedReader(new InputStreamReader(new ByteArrayInputStream(input.getBytes()))));
//            Gallows gallows = new Gallows(new Word("drift", "this do every body in NFS"), 5);
//
//            GameState moveResult = player.makeMove(gallows);
//            assertThat(moveResult).isInstanceOf(IncorrectState.class);
//            assertThat(moveResult.terminal()).isFalse();
//            assertThat(moveResult.message()).isEqualTo(GameInterface.INCORRECT_GUESS);
//
//            assertThat(gallows.showHint()).isEqualTo(false);
//            assertThat(byteArrayOutputStream.toString().trim()).isEqualTo("You print letter: A");
//            byteArrayOutputStream.reset();
//
//            GameState moveResult1 = player.makeMove(gallows);
//            assertThat(moveResult1).isInstanceOf(HintRequestState.class);
//            assertThat(moveResult1.terminal()).isFalse();
//            assertThat(moveResult1.message()).isEqualTo(GameInterface.HINT_GUESS);
//
//            assertThat(gallows.showHint()).isEqualTo(true);
//
//            GameState moveResult2 = player.makeMove(gallows);
//
//            assertThat(moveResult2).isInstanceOf(HintRequestState.class);
//            assertThat(moveResult2.terminal()).isFalse();
//            assertThat(moveResult2.message()).isEqualTo(GameInterface.HINT_GUESS);
//
//            assertThat(gallows.showHint()).isEqualTo(true);
//
//            GameState moveResult3 = player.makeMove(gallows);
//
//            assertThat(moveResult3).isInstanceOf(HintRequestState.class);
//            assertThat(moveResult3.terminal()).isFalse();
//            assertThat(moveResult3.message()).isEqualTo(GameInterface.HINT_GUESS);
//
//            assertThat(gallows.showHint()).isEqualTo(true);
//
//            GameState moveResult4 = player.makeMove(gallows);
//
//            assertThat(moveResult4).isInstanceOf(HintRequestState.class);
//            assertThat(moveResult4.terminal()).isFalse();
//            assertThat(moveResult4.message()).isEqualTo(GameInterface.HINT_GUESS);
//
//            assertThat(gallows.showHint()).isEqualTo(true);
//
//            GameState moveResult5 = player.makeMove(gallows);
//
//            assertThat(moveResult5).isInstanceOf(HintRequestState.class);
//            assertThat(moveResult5.terminal()).isFalse();
//            assertThat(moveResult5.message()).isEqualTo(GameInterface.HINT_GUESS);
//
//            assertThat(gallows.showHint()).isEqualTo(true);
//
//            assertThatThrownBy(() -> player.makeMove(gallows));
//            assertThat(gallows.showHint()).isEqualTo(true);
//
//            assertThat(byteArrayOutputStream.toString().trim()).isEqualTo(
//                GameInterface.ERROR + GameInterface.INPUT_WORD_ERROR);
//        }
//
//        @Test void testHintInGame() throws IOException {
//            String input = "1\n4\ne\nHINT";
//            System.setIn(new ByteArrayInputStream(input.getBytes()));
//            Game game = new Game();
//            assertThatThrownBy(game::run);
//            String expected = start + """
//                1. GAMES
//                You chose GAMES
//                """ + GameInterface.DIFFICULTY_MSG + "\n" + GameInterface.NEXT_MOVE + "\n" + """
//                Word category is GAMES
//                """ + GameInterface.BASE_HINT_MSG + "\n" + """
//                -----
//                |   |
//                |   O
//                |  /|\\
//                |
//                |
//                --------
//
//                Attempts Left: 4
//                Word: _ _ _ _
//                You print letter: E
//                """ + GameInterface.INCORRECT_GUESS + "\n" + GameInterface.NEXT_MOVE + "\n" + """
//                Word category is GAMES
//                """ + GameInterface.BASE_HINT_MSG + "\n" + """
//                -----
//                |   |
//                |   O
//                |  /|\\
//                |  /
//                |
//                --------
//
//                Attempts Left: 3
//                Word: _ _ _ _
//                """ + GameInterface.HINT_GUESS + "\n" + GameInterface.NEXT_MOVE + "\n" + """
//                Word category is GAMES
//                HINT: new Valve game
//                -----
//                |   |
//                |   O
//                |  /|\\
//                |  /
//                |
//                --------
//
//                Attempts Left: 3
//                Word: _ _ _ _
//                """.trim();
//            String realOutput = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
//            assertThat(realOutput).isEqualTo(expected);
//        }
//
//        @Test void runConsoleGameWinTest() throws IOException {
//            String input = """
//                1
//                4
//                d
//                a
//                """;
//            System.setIn(new ByteArrayInputStream(input.getBytes()));
//            Game game = new Game();
//            GameState moveResult = game.run();
//            String expectedOut = start + """
//                1. GAMES
//                You chose GAMES
//                """ + GameInterface.DIFFICULTY_MSG + "\n" + GameInterface.NEXT_MOVE + "\n" + """
//                Word category is GAMES
//                """ + GameInterface.BASE_HINT_MSG + "\n" + """
//                -----
//                |   |
//                |   O
//                |  /|\\
//                |
//                |
//                --------
//
//                Attempts Left: 4
//                Word: _ _ _ _
//                You print letter: D
//                """ + GameInterface.CORRECT_GUESS + "\n" + GameInterface.NEXT_MOVE + "\n" + """
//                Word category is GAMES
//                """ + GameInterface.BASE_HINT_MSG + "\n" + """
//                -----
//                |   |
//                |   O
//                |  /|\\
//                |
//                |
//                --------
//
//                Attempts Left: 4
//                Word: D _ D _
//                You print letter: A
//                Word category is GAMES
//                """ + GameInterface.BASE_HINT_MSG + "\n" + """
//                -----
//                |   |
//                |   O
//                |  /|\\
//                |
//                |
//                --------
//
//                Attempts Left: 4
//                Word: D A D A
//                You guessed the word, congratulations!
//                Answer is DADA
//                """ + GameInterface.END_MSG;
//            String realOutput = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
//
//            assertThat(moveResult).isInstanceOf(WinState.class);
//            assertThat(realOutput.length()).isEqualTo(expectedOut.trim().length());
//            assertThat(realOutput).isEqualTo(expectedOut.trim());
//        }
//
//        @Test void runConsoleGameLooseTest() throws IOException {
//            String input = """
//                1
//                3
//                a
//                b
//                c
//                e
//                k
//                """;
//            System.setIn(new ByteArrayInputStream(input.getBytes()));
//            Game game = new Game();
//            GameState moveResult = game.run();
//            String expectedOut = start + """
//                1. GAMES
//                You chose GAMES
//                """ + GameInterface.DIFFICULTY_MSG + "\n" + GameInterface.NEXT_MOVE + "\n" + """
//                Word category is GAMES
//                """ + GameInterface.BASE_HINT_MSG + "\n" + """
//                -----
//                |   |
//                |   O
//                |  /|\\
//                |  /
//                |
//                --------
//
//                Attempts Left: 3
//                Word: _ _ _ _
//                You print letter: A
//                """ + GameInterface.CORRECT_GUESS + "\n" + GameInterface.NEXT_MOVE + "\n" + """
//                Word category is GAMES
//                """ + GameInterface.BASE_HINT_MSG + "\n" + """
//                -----
//                |   |
//                |   O
//                |  /|\\
//                |  /
//                |
//                --------
//
//                Attempts Left: 3
//                Word: _ A _ A
//                You print letter: B
//                """ + GameInterface.INCORRECT_GUESS + "\n" + GameInterface.NEXT_MOVE + "\n" + """
//                Word category is GAMES
//                """ + GameInterface.BASE_HINT_MSG + "\n" + """
//                -----
//                |   |
//                |   O
//                |  /|\\
//                |  / \\
//                |
//                --------
//
//                Attempts Left: 2
//                Word: _ A _ A
//                You print letter: C
//                """ + GameInterface.INCORRECT_GUESS + "\n" + GameInterface.NEXT_MOVE + "\n" + """
//                Word category is GAMES
//                """ + GameInterface.BASE_HINT_MSG + "\n" + """
//                -----
//                |   |
//                |   O
//                |  /|\\\\
//                |  / \\
//                |
//                --------
//
//                Attempts Left: 1
//                Word: _ A _ A
//                You print letter: E
//                Word category is GAMES
//                """ + GameInterface.BASE_HINT_MSG + "\n" + """
//                -----
//                |   |
//                |   O
//                |  /|\\\\
//                |  / \\\\
//                |
//                --------
//
//                Attempts Left: 0
//                Word: _ A _ A
//                Unfortunate, you not guessed the word!
//                Answer is DADA
//                """ + GameInterface.END_MSG;
//            String realOutput = byteArrayOutputStream.toString().replace("\r\n", "\n").replace("\r", "\n").trim();
//            assertThat(moveResult).isInstanceOf(LooseState.class);
////            assertThat(realOutput.length()).isEqualTo(expectedOut.trim().length());
//            assertThat(realOutput).isEqualTo(expectedOut.trim());
//        }
//    }
//}
