package backend.academy.gallows;

import backend.academy.game.Gallows;
import backend.academy.game.dictionary.Word;
import backend.academy.game.states.CorrectState;
import backend.academy.game.states.GameState;
import backend.academy.game.states.IncorrectState;
import backend.academy.game.states.LooseState;
import backend.academy.game.states.RepeatState;
import backend.academy.game.states.UtilStateMessages;
import backend.academy.game.states.WinState;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class GallowsTest {

    @Nested class GuessingWord {

        @Test void testLetterCase() {
            Gallows gallows = new Gallows(new Word("dino", "lived 65 mil years ago"), 5);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"_", "_", "_", "_"});
            GameState result = gallows.makeMove("o");
            assertThat(result.terminal()).isFalse();
            assertThat(result).isNotInstanceOf(RepeatState.class); // wasn't used before

            assertThat(gallows.getWordState()).isEqualTo(new String[] {"_", "_", "_", "O"});
            GameState result1 = gallows.makeMove("O");
            assertThat(result.terminal()).isFalse();
            assertThat(result1).isInstanceOf(RepeatState.class); // already was used;
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"_", "_", "_", "O"});

            GameState result2 = gallows.makeMove("T");
            assertThat(result2.terminal()).isFalse();
            assertThat(result2).isNotInstanceOf(RepeatState.class); // wasn't used before
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"_", "_", "_", "O"});

            GameState result3 = gallows.makeMove("t");
            assertThat(result3.terminal()).isFalse();
            assertThat(result3).isInstanceOf(RepeatState.class); // wasn't used before
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"_", "_", "_", "O"});
        }

        @Test void testGuessingResult() {
            Gallows gallows = new Gallows(new Word("father", "one of parent"), 5);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"_", "_", "_", "_", "_", "_"});

            GameState result1 = gallows.makeMove("F");
            assertThat(result1.terminal()).isFalse();
            assertThat(result1).isInstanceOf(CorrectState.class);
            assertThat(result1.message()).isEqualTo(UtilStateMessages.CORRECT_STATE);

            assertThat(gallows.getWordState()).isEqualTo(new String[] {"F", "_", "_", "_", "_", "_"});

            GameState result2 = gallows.makeMove("F");

            assertThat(result2.terminal()).isFalse();
            assertThat(result2).isInstanceOf(RepeatState.class);
            assertThat(result2.message()).isEqualTo(UtilStateMessages.REPEAT_STATE);

            assertThat(gallows.getWordState()).isEqualTo(new String[] {"F", "_", "_", "_", "_", "_"});

            GameState result3 = gallows.makeMove("f");

            assertThat(result3.terminal()).isFalse();
            assertThat(result3).isInstanceOf(RepeatState.class);
            assertThat(result3.message()).isEqualTo(UtilStateMessages.REPEAT_STATE);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"F", "_", "_", "_", "_", "_"});

            GameState result4 = gallows.makeMove("a");

            assertThat(result4.terminal()).isFalse();
            assertThat(result4).isNotInstanceOf(RepeatState.class);
            assertThat(result4).isInstanceOf(CorrectState.class);
            assertThat(result4.message()).isEqualTo(UtilStateMessages.CORRECT_STATE);

            assertThat(gallows.getWordState()).isEqualTo(new String[] {"F", "A", "_", "_", "_", "_"});

            GameState result5 = gallows.makeMove("o");

            assertThat(result5.terminal()).isFalse();
            assertThat(result5).isNotInstanceOf(CorrectState.class);
            assertThat(result5).isInstanceOf(IncorrectState.class);
            assertThat(result5.message()).isNotEqualTo(UtilStateMessages.CORRECT_STATE);
            assertThat(result5.message()).isEqualTo(UtilStateMessages.INCORRECT_STATE);

            assertThat(gallows.getWordState()).isEqualTo(new String[] {"F", "A", "_", "_", "_", "_"});

            GameState result6 = gallows.makeMove("o");

            assertThat(result6.terminal()).isFalse();
            assertThat(result6).isNotInstanceOf(CorrectState.class);
            assertThat(result6).isInstanceOf(RepeatState.class);
            assertThat(result6.message()).isNotEqualTo(UtilStateMessages.CORRECT_STATE);
            assertThat(result6.message()).isEqualTo(UtilStateMessages.REPEAT_STATE);

            assertThat(gallows.getWordState()).isEqualTo(new String[] {"F", "A", "_", "_", "_", "_"});

            GameState result7 = gallows.makeMove("O");
            assertThat(result7.terminal()).isFalse();
            assertThat(result7).isInstanceOf(RepeatState.class);
            assertThat(result7.message()).isEqualTo(UtilStateMessages.REPEAT_STATE);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"F", "A", "_", "_", "_", "_"});
        }

        @Test void testLooseResult() {
            Gallows gallows = new Gallows(new Word("drift", "this do every body in NFS"), 5);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"_", "_", "_", "_", "_"});

            GameState result = gallows.makeMove("d");
            assertThat(result.terminal()).isFalse();
            assertThat(result).isInstanceOf(CorrectState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "_", "_", "_", "_"});
            assertThat(gallows.wrongGuess()).isEqualTo(0);
            GameState result1 = gallows.makeMove("a");
            assertThat(result1.terminal()).isFalse();
            assertThat(result1).isInstanceOf(IncorrectState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "_", "_", "_", "_"});
            assertThat(gallows.wrongGuess()).isEqualTo(1);
            GameState result2 = gallows.makeMove("D");
            assertThat(result2.terminal()).isFalse();
            assertThat(result2).isInstanceOf(RepeatState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "_", "_", "_", "_"});
            assertThat(gallows.wrongGuess()).isEqualTo(1);
            GameState result3 = gallows.makeMove("B");
            assertThat(result3.terminal()).isFalse();
            assertThat(result3).isInstanceOf(IncorrectState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "_", "_", "_", "_"});
            assertThat(gallows.wrongGuess()).isEqualTo(2);
            GameState result4 = gallows.makeMove("C");
            assertThat(result4.terminal()).isFalse();
            assertThat(result4).isInstanceOf(IncorrectState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "_", "_", "_", "_"});
            assertThat(gallows.wrongGuess()).isEqualTo(3);
            GameState result5 = gallows.makeMove("M");
            assertThat(result5.terminal()).isFalse();
            assertThat(result5).isInstanceOf(IncorrectState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "_", "_", "_", "_"});
            assertThat(gallows.wrongGuess()).isEqualTo(4);
            GameState result6 = gallows.makeMove("L");
            assertThat(result6.terminal()).isTrue();
            assertThat(result6).isNotInstanceOf(IncorrectState.class);
            assertThat(result6).isInstanceOf(LooseState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "_", "_", "_", "_"});
            assertThat(gallows.wrongGuess()).isEqualTo(5);
            // knowing that state are loose
            GameState result7 = gallows.makeMove("J");
            assertThat(result7.terminal()).isTrue();
            assertThat(result7).isNotInstanceOf(IncorrectState.class);
            assertThat(result7).isInstanceOf(LooseState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "_", "_", "_", "_"});
            assertThat(gallows.wrongGuess()).isEqualTo(5); // END WAS BEFORE
        }

        @Test void testWinResult() {
            Gallows gallows = new Gallows(new Word("drift", "this do everybody in NFS"), 5);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"_", "_", "_", "_", "_"});

            GameState result = gallows.makeMove("d");
            assertThat(result.terminal()).isFalse();
            assertThat(result).isInstanceOf(CorrectState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "_", "_", "_", "_"});

            GameState result1 = gallows.makeMove("a");
            assertThat(result1.terminal()).isFalse();
            assertThat(result1).isInstanceOf(IncorrectState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "_", "_", "_", "_"});

            GameState result2 = gallows.makeMove("B");
            assertThat(result2.terminal()).isFalse();
            assertThat(result2).isInstanceOf(IncorrectState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "_", "_", "_", "_"});

            GameState result3 = gallows.makeMove("b");
            assertThat(result3.terminal()).isFalse();
            assertThat(result3).isInstanceOf(RepeatState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "_", "_", "_", "_"});

            GameState result4 = gallows.makeMove("D");
            assertThat(result4.terminal()).isFalse();
            assertThat(result4).isInstanceOf(RepeatState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "_", "_", "_", "_"});

            GameState result5 = gallows.makeMove("R");
            assertThat(result5.terminal()).isFalse();
            assertThat(result5).isInstanceOf(CorrectState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "R", "_", "_", "_"});

            GameState result6 = gallows.makeMove("I");
            assertThat(result6.terminal()).isFalse();
            assertThat(result6).isInstanceOf(CorrectState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "R", "I", "_", "_"});

            GameState result7 = gallows.makeMove("F");
            assertThat(result7.terminal()).isFalse();
            assertThat(result7).isInstanceOf(CorrectState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "R", "I", "F", "_"});

            GameState result8 = gallows.makeMove("T");
            assertThat(result8.terminal()).isTrue();
            assertThat(result8).isNotInstanceOf(CorrectState.class);
            assertThat(result8).isInstanceOf(WinState.class);
            assertThat(gallows.getWordState()).isEqualTo(new String[] {"D", "R", "I", "F", "T"});
        }
    }
}
