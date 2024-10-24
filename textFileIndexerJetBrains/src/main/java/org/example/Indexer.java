package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class Indexer {
    private final Map<String, Map<String, Integer>> index = new HashMap<>();
    private final Tokenizer tokenizer;

    public Indexer(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public void addPath(String pathStr) throws IOException {
        Path path = Paths.get(pathStr);
        if (Files.exists(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) {
                    if (Files.isRegularFile(filePath) && filePath.toString().endsWith(".txt")) {
                        try {
                            indexFile(filePath);
                        } catch (IOException e) {
                            System.err.println("Error indexing file: " + filePath.toString());
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } else {
            System.out.println("The specified path does not exist.");
        }
    }

    private void indexFile(Path filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line;
            Map<String, Integer> fileIndex = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                for (String word : tokenizer.tokenize(line.toLowerCase())) {
                    fileIndex.put(word, fileIndex.getOrDefault(word, 0) + 1);
                }
            }
            index.put(filePath.toAbsolutePath().toString(), fileIndex);
        }
    }

    public List<String> searchFilesContainingWord(String word) {
        word = word.toLowerCase();
        List<String> filesContainingWord = new ArrayList<>();
        for (Map.Entry<String, Map<String, Integer>> entry : index.entrySet()) {
            if (entry.getValue().containsKey(word)) {
                filesContainingWord.add(entry.getKey());
            }
        }
        return filesContainingWord;
    }

    public void search(String word) {
        List<String> results = searchFilesContainingWord(word);
        if (!results.isEmpty()) {
            System.out.println("Files containing '" + word + "':");
            for (String filePath : results) {
                System.out.println("- " + filePath);
            }
        } else {
            System.out.println("No files contain the word '" + word + "'.");
        }
    }

    public Map<String, Map<String, Integer>> getIndex() {
        return index;
    }
}
