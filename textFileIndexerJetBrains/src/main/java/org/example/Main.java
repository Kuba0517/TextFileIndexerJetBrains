package org.example;

import org.example.Indexer;
import org.example.Tokenizer;
import org.example.TokenizerImpl;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static final Indexer indexer = new Indexer(new TokenizerImpl());

    public static void main(String[] args) {
        System.out.println("Welcome to the Text File Indexer!");
        System.out.println("Type 'help' for a list of commands.\n");

        Scanner scanner = new Scanner(System.in);
        String command;

        while (true) {
            System.out.print("> ");
            command = scanner.nextLine().trim();
            if (command.equalsIgnoreCase("exit")) {
                System.out.println("Goodbye!");
                break;
            } else if (command.startsWith("add ")) {
                String path = command.substring(4).trim();
                addPath(path);
            } else if (command.startsWith("search ")) {
                String word = command.substring(7).trim();
                indexer.search(word);
            } else if (command.equalsIgnoreCase("help")) {
                printHelp();
            } else {
                System.out.println("Unknown command. Type 'help' to see all commands.");
            }
        }

        scanner.close();
    }

    private static void addPath(String pathStr) {
        try {
            System.out.println("Indexing files. Please wait...");
            indexer.addPath(pathStr);
            System.out.println("Indexing has finished.");
        } catch (IOException e) {
            System.out.println("Error during files indexing " + e.getMessage());
        }
    }

    private static void printHelp() {
        System.out.println("Available commands:");
        System.out.println("add [path]     - Add a file or directory to the index.");
        System.out.println("search [word]  - Search for files containing the word.");
        System.out.println("exit           - Exit the application.");
    }
}
