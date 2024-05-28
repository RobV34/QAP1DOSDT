package com.keyin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class SuggestionEngineTest {
    private SuggestionEngine suggestionEngine;

    @BeforeEach
    void setUp() {
        suggestionEngine = new SuggestionEngine();
        SuggestionsDatabase db = new SuggestionsDatabase();
        db.getWordMap().put("test", 1);
        db.getWordMap().put("hello", 1);
        db.getWordMap().put("world", 1);
        suggestionEngine.setWordSuggestionDB(db);
    }

    @Test
    void testGenerateSuggestions_CorrectWord() {
        String result = suggestionEngine.generateSuggestions("test");
        assertEquals("", result, "If the word is correct, the result should be an empty string.");
    }

    @Test
    void testGenerateSuggestions_IncorrectWord() {
        String result = suggestionEngine.generateSuggestions("helo");
        assertTrue(result.contains("hello"), "The suggestion list should contain 'hello' for the input 'helo'.");
    }

    @Test
    void testLoadDictionaryData() throws Exception {
        Path path = getPathFromResource("words.txt");
        suggestionEngine.loadDictionaryData(path);
        assertTrue(suggestionEngine.getWordSuggestionDB().containsKey("example"), "The word 'example' should be loaded into the dictionary.");
    }

    @Test
    void testGenerateSuggestions_EmptyInput() {
        String result = suggestionEngine.generateSuggestions("");
        assertEquals("", result, "For an empty input, the result should be an empty string.");
    }

    @Test
    void testGenerateSuggestions_NoSuggestions() {
        String result = suggestionEngine.generateSuggestions("zzzzzz");
        assertEquals("", result, "For a word with no suggestions, the result should be an empty string.");
    }

    @Test
    void testGenerateSuggestions_MultipleSuggestions() {
        SuggestionsDatabase db = new SuggestionsDatabase();
        db.getWordMap().put("cat", 1);
        db.getWordMap().put("bat", 1);
        db.getWordMap().put("rat", 1);
        db.getWordMap().put("hat", 1);
        suggestionEngine.setWordSuggestionDB(db);

        String result = suggestionEngine.generateSuggestions("tat");
        assertTrue(result.contains("cat"), "The suggestion list should contain 'cat' for the input 'tat'.");
        assertTrue(result.contains("bat"), "The suggestion list should contain 'bat' for the input 'tat'.");
        assertTrue(result.contains("rat"), "The suggestion list should contain 'rat' for the input 'tat'.");
        assertTrue(result.contains("hat"), "The suggestion list should contain 'hat' for the input 'tat'.");
    }

    private Path getPathFromResource(String fileName) throws URISyntaxException {
        return Paths.get(ClassLoader.getSystemResource(fileName).toURI());
    }
}


