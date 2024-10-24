package org.example;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class IndexerTest {

    private static Indexer indexer;
    private static String testFilesPath = "test-files";

    @BeforeAll
    public static void setUp() throws IOException {
        Tokenizer tokenizer = new TokenizerImpl();
        indexer = new Indexer(tokenizer);
        indexer.addPath(testFilesPath);
    }

    @Test
    public void testIndexing() {
        Map<String, Map<String, Integer>> index = indexer.getIndex();
        assertEquals(4, index.size());
        assertTrue(index.containsKey(Paths.get("test-files/testFile1.txt").toAbsolutePath().toString()));
        assertTrue(index.containsKey(Paths.get("test-files/testFile2.txt").toAbsolutePath().toString()));
        assertTrue(index.containsKey(Paths.get("test-files/testFile3.txt").toAbsolutePath().toString()));
        assertTrue(index.containsKey(Paths.get("test-files/testFile4.txt").toAbsolutePath().toString()));
    }

    @Test
    public void testSearchExistingWord() {
        List<String> results = indexer.searchFilesContainingWord("email");
        assertEquals(2, results.size());
        assertTrue(results.contains(Paths.get("test-files/testFile1.txt").toAbsolutePath().toString()));
        assertTrue(results.contains(Paths.get("test-files/testFile4.txt").toAbsolutePath().toString()));
    }

    @Test
    public void testSearchNonExistingWord() {
        List<String> results = indexer.searchFilesContainingWord("yyyyyyyxxxxxxxxxzzzzzzzzz");
        assertTrue(results.isEmpty());
    }

    @Test
    public void testSearchWithContractions() {
        List<String> results = indexer.searchFilesContainingWord("shouldn't");
        assertTrue(results.isEmpty());

        results = indexer.searchFilesContainingWord("should");
        assertEquals(1, results.size());
        assertTrue(results.contains(Paths.get("test-files/testFile3.txt").toAbsolutePath().toString()));

        results = indexer.searchFilesContainingWord("not");
        assertEquals(1, results.size());
        assertTrue(results.contains(Paths.get("test-files/testFile3.txt").toAbsolutePath().toString()));
    }

    @Test
    public void testSearchEmailsAndURLs() {
        List<String> results = indexer.searchFilesContainingWord("jakub.ratuszniak1@gmail.com");
        assertEquals(1, results.size());
        assertTrue(results.contains(Paths.get("test-files/testFile1.txt").toAbsolutePath().toString()));

        results = indexer.searchFilesContainingWord("https://github.com/Kuba0517");
        assertEquals(1, results.size());
        assertTrue(results.contains(Paths.get("test-files/testFile3.txt").toAbsolutePath().toString()));
    }
}

