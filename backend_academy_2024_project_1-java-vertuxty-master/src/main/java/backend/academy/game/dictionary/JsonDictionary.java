package backend.academy.game.dictionary;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.Getter;

public class JsonDictionary {
    private Map<WordDifficulty, Map<String, List<Word>>> dictionary;
    @Getter private ArrayList<WordDifficulty> difficulties;
    private final SecureRandom random;
    public static final int UPPER_BOUND_LEN = 17;
    public static final int LOWER_BOUND_LEN = 3;

    public JsonDictionary() {
        this.random = new SecureRandom();
    }

    public void load() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        this.dictionary = objectMapper.readValue(new File(UtilDictionary.jsonPath), new TypeReference<>() {
        });
        ArrayList<WordDifficulty> list = new ArrayList<>(dictionary.keySet());
        Collections.sort(list);
        this.difficulties = new ArrayList<>(list);
    }

    public List<String> getGroupsByDifficulty(WordDifficulty wordDifficulty) {
        List<String> groups = new ArrayList<>(dictionary.get(wordDifficulty).keySet());
        Collections.sort(groups);
        return groups;
    }

    public String getGroup(String group, WordDifficulty wordDifficulty) {
        List<String> groups = new ArrayList<>(dictionary.get(wordDifficulty).keySet());
        Collections.sort(groups);
        if (group.isEmpty()) {
            return groups.get(random.nextInt(0, groups.size()));
        }
        return groups.get(Integer.parseInt(group) - 1);
    }

    public Word getWordByGroupAndDifficulty(String group, WordDifficulty wordDifficulty) {
        List<Word> words = dictionary.get(wordDifficulty).get(group.toUpperCase());
        if (words.isEmpty()) {
            throw new IllegalArgumentException("Wrong count of words");
        }
        Word word = words.get(random.nextInt(0, words.size()));
        if (word.word().length() > UPPER_BOUND_LEN || word.word().length() < LOWER_BOUND_LEN) {
            throw new IllegalArgumentException("Wrong word length. It must not be empty and length leq then 20");
        }
        return word;
    }

    public WordDifficulty getDifficulty(String difficultyInput) {
        if (difficultyInput.isEmpty()) {
            return difficulties.get(random.nextInt(0, difficulties.size()));
        }
        return difficulties.get(Integer.parseInt(difficultyInput) - 1);
    }

    public boolean isEmpty() {
        return difficulties.isEmpty();
    }
}

