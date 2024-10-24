package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class TokenizerTest {

    @Test
    void testTokenizer() {
        Tokenizer tokenizer = new TokenizerImpl();
        String text = "Welcome to my test! This is my email: jakub.ratuszniak1@gmail.com. Current date isn't 23/10/2024." +
                "My favourite number is 3.14.";
        List<String> tokens = tokenizer.tokenize(text);
        List<String> expected = Arrays.asList("welcome", "to", "my", "test", "this", "is", "my", "email",
                "jakub.ratuszniak1@gmail.com", "current", "date", "is", "not", "23/10/2024", "my", "favourite", "number",
                "is", "3.14");
        assertEquals(expected, tokens);
    }
}