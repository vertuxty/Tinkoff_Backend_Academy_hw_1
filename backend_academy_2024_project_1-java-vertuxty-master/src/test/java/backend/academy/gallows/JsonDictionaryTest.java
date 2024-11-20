package backend.academy.gallows;

import backend.academy.game.dictionary.JsonDictionary;
import backend.academy.game.dictionary.UtilDictionary;
import backend.academy.game.dictionary.Word;
import backend.academy.game.dictionary.WordDifficulty;
import java.util.List;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JsonDictionaryTest {

    @Nested class WordTest {

        @Test
        @Order(1)
        void dictionaryTest() {

            final JsonDictionary jsonDictionary = getJsonDictionary();
            assertThatCode(jsonDictionary::load).doesNotThrowAnyException();
            assertThatThrownBy(() -> jsonDictionary.getDifficulty("-1"));
            assertThatThrownBy(() -> jsonDictionary.getDifficulty("-2"));
            assertThatThrownBy(() -> jsonDictionary.getDifficulty("a"));
            assertThatThrownBy(() -> jsonDictionary.getDifficulty("adsa21213"));
            assertThatThrownBy(() -> jsonDictionary.getDifficulty("10000"));

            WordDifficulty wordDifficulty = jsonDictionary.getDifficulty("1");
            assertThat(wordDifficulty).isEqualTo(WordDifficulty.EASY);
            List<String> easyGroups = jsonDictionary.getGroupsByDifficulty(wordDifficulty);
            assertThat(easyGroups.isEmpty()).isFalse();
            assertThat(easyGroups).isNotEqualTo(List.of("ANIME", "ANIME", "FILMS"));
            assertThat(easyGroups).contains("ANIME");
            assertThat(easyGroups).doesNotContain("FILMS");
            assertThat(easyGroups).isEqualTo(List.of("ANIME"));

            WordDifficulty wordDifficulty1 = jsonDictionary.getDifficulty("2");
            assertThat(wordDifficulty1).isEqualTo(WordDifficulty.MEDIUM);

            List<String> easyGroups1 = jsonDictionary.getGroupsByDifficulty(wordDifficulty1);
            assertThat(easyGroups1.isEmpty()).isFalse();
            assertThat(easyGroups1).isNotEqualTo(List.of("ANIME", "ANIME", "FILMS"));
            assertThat(easyGroups1).contains("ANIME");
            assertThat(easyGroups1).contains("FILMS");
            assertThat(easyGroups1).isEqualTo(List.of("ANIME", "FILMS"));

            WordDifficulty wordDifficulty2 = jsonDictionary.getDifficulty("3");
            assertThat(wordDifficulty2).isEqualTo(WordDifficulty.HARD);

            List<String> easyGroups2 = jsonDictionary.getGroupsByDifficulty(wordDifficulty2);
            assertThat(easyGroups2).doesNotContain("ANIME");
            assertThat(easyGroups2).doesNotContain("FILMS");
            assertThat(easyGroups2.isEmpty()).isTrue();
        }

        @Test
        @Order(2)
        void testIncorrectWordLength() {

            UtilDictionary.jsonPath = "src/test/java/backend/academy/gallows/resources/lengthWordTest.json";
            JsonDictionary jsonDictionary = new JsonDictionary();
            assertThatCode(jsonDictionary::load).doesNotThrowAnyException();
            WordDifficulty wordDifficulty = jsonDictionary.getDifficulty("1");
            assertThat(wordDifficulty).isEqualTo(WordDifficulty.EASY);
            List<String> groups = jsonDictionary.getGroupsByDifficulty(wordDifficulty);
            assertThat(groups.size()).isEqualTo(3);

            assertThatThrownBy(() -> jsonDictionary.getWordByGroupAndDifficulty(groups.get(1), wordDifficulty));
            assertThatCode(() -> jsonDictionary.getWordByGroupAndDifficulty(groups.getFirst(),
                wordDifficulty)).doesNotThrowAnyException();
            assertThatThrownBy(() -> jsonDictionary.getWordByGroupAndDifficulty(groups.get(2), wordDifficulty));
        }

        @Test
        void testGetDifficulties() {
            UtilDictionary.jsonPath = "src/test/java/backend/academy/gallows/resources/testWords.json";
            JsonDictionary jsonDictionary = new JsonDictionary();
            assertThatCode(jsonDictionary::load).doesNotThrowAnyException();
            List<WordDifficulty> wordDifficulties = jsonDictionary.difficulties();
            assertThat(wordDifficulties.isEmpty()).isFalse();
            assertThat(wordDifficulties).contains(WordDifficulty.EASY, WordDifficulty.MEDIUM, WordDifficulty.HARD);
            assertThat(wordDifficulties).isNotEqualTo(
                List.of(WordDifficulty.EASY, WordDifficulty.HARD, WordDifficulty.MEDIUM));
            assertThat(wordDifficulties).isEqualTo(
                List.of(WordDifficulty.EASY, WordDifficulty.MEDIUM, WordDifficulty.HARD));
            UtilDictionary.jsonPath = "src/test/java/backend/academy/gallows/resources/consoleTest.json";
            JsonDictionary jsonDictionary1 = new JsonDictionary();
            assertThatCode(jsonDictionary1::load).doesNotThrowAnyException();
            List<WordDifficulty> wordDifficulties1 = jsonDictionary1.difficulties();
            assertThat(wordDifficulties1.isEmpty()).isFalse();
            assertThat(wordDifficulties1).doesNotContain(WordDifficulty.MEDIUM);
            assertThat(wordDifficulties1).contains(WordDifficulty.EASY, WordDifficulty.HARD);
            assertThat(wordDifficulties1).isEqualTo(List.of(WordDifficulty.EASY, WordDifficulty.HARD));
        }

        @Test
        void testGetGroup() {
            UtilDictionary.jsonPath = "src/test/java/backend/academy/gallows/resources/lengthWordTest.json";
            JsonDictionary jsonDictionary = new JsonDictionary();
            assertThatCode(jsonDictionary::load).doesNotThrowAnyException();
            WordDifficulty wordDifficulty = jsonDictionary.getDifficulty("1");
            assertThatThrownBy(() -> jsonDictionary.getGroup("-1", wordDifficulty));
            assertThatThrownBy(() -> jsonDictionary.getGroup("aaasa", wordDifficulty));
            assertThatThrownBy(() -> jsonDictionary.getGroup("-asdasd", wordDifficulty));
            assertThatThrownBy(() -> jsonDictionary.getGroup("1000000", wordDifficulty));
            assertThat(jsonDictionary.getGroupsByDifficulty(wordDifficulty).size()).isEqualTo(3);
            assertThatCode(() -> jsonDictionary.getGroup("1", wordDifficulty)).doesNotThrowAnyException();
            assertThat(jsonDictionary.getGroup("1", wordDifficulty)).isEqualTo("ANIME");
            assertThatCode(() -> jsonDictionary.getGroup("2", wordDifficulty)).doesNotThrowAnyException();
            assertThat(jsonDictionary.getGroup("2", wordDifficulty)).isEqualTo("FILMS");
            assertThatCode(() -> jsonDictionary.getGroup("3", wordDifficulty)).doesNotThrowAnyException();
            assertThat(jsonDictionary.getGroup("3", wordDifficulty)).isEqualTo("GAMES");
        }

        @Test
        void testCorrectWordGroup() {
            UtilDictionary.jsonPath = "src/test/java/backend/academy/gallows/resources/lengthWordTest.json";
            JsonDictionary jsonDictionary = new JsonDictionary();
            assertThatCode(jsonDictionary::load).doesNotThrowAnyException();
            WordDifficulty wordDifficulty = jsonDictionary.getDifficulty("1");
            assertThat(wordDifficulty).isEqualTo(WordDifficulty.EASY);
            String group = jsonDictionary.getGroup("1", wordDifficulty);
            Word word = jsonDictionary.getWordByGroupAndDifficulty(group, wordDifficulty);
            assertThat(word.word()).isNotEqualTo("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            assertThat(word.word()).isEqualTo("aaaaaa");
        }

        @Test
        void testIncorrectJsonTest() {
            UtilDictionary.jsonPath = "incorrectPath";
            JsonDictionary jsonDictionary = new JsonDictionary();
            assertThatThrownBy(jsonDictionary::load);
        }

        @Test
        void testRandomDifficulty() {
            JsonDictionary jsonDictionary = getJsonDictionary();
            assertThatCode(jsonDictionary::load).doesNotThrowAnyException();
            for (int i = 0; i < 10; i++) {
                assertThatCode(() -> jsonDictionary.getDifficulty("")).doesNotThrowAnyException();

                WordDifficulty difficulty = jsonDictionary.getDifficulty("");
                List<WordDifficulty> difficulties = jsonDictionary.difficulties();
                assertThat(difficulties).contains(difficulty);
            }
        }

        @Test
        void testRandomGroup() {
            JsonDictionary jsonDictionary = getJsonDictionary();
            assertThatCode(jsonDictionary::load).doesNotThrowAnyException();
            WordDifficulty difficulty = jsonDictionary.getDifficulty("2");
            List<String> groups = jsonDictionary.getGroupsByDifficulty(difficulty);
            for (int i = 0; i < 10; i++) {
                assertThatCode(() -> jsonDictionary.getGroup("", difficulty)).doesNotThrowAnyException();
                String group = jsonDictionary.getGroup("", difficulty);
                assertThat(groups).contains(group);
            }
        }
    }

    private static JsonDictionary getJsonDictionary() {
        UtilDictionary.jsonPath = "src/test/java/backend/academy/gallows/resources/testWords.json";
        return new JsonDictionary();
    }
}
