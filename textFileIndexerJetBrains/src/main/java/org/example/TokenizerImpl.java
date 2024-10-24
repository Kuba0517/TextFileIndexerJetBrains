package org.example;

import org.example.Tokenizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TokenizerImpl implements Tokenizer {
    private static final String EMAIL_REGEX = "\\b[\\w.%+-]+@[\\w.-]+\\.[A-Za-z]{2,6}\\b";
    private static final String URL_REGEX = "\\bhttps?://[\\w.-]+(?:\\.[\\w.-]+)+(?:/\\S*)?\\b";
    private static final String DATE_REGEX = "\\b\\d{1,2}[/-]\\d{1,2}[/-]\\d{2,4}\\b|\\b\\w+ \\d{1,2}, \\d{4}\\b";
    private static final String NUMBER_REGEX = "\\b\\d+(?:\\.\\d+)?\\b";
    private static final String ABBREVIATION_REGEX = "\\b(?:[A-Za-z]\\.){2,}";
    private static final String CONTRACTION_REGEX = "\\b\\w+'(?:\\w+)?\\b";
    private static final String WORD_REGEX = "\\b\\w+(?:-\\w+)*\\b";

    private static final Pattern TOKEN_PATTERN = Pattern.compile(
            EMAIL_REGEX + "|" + URL_REGEX + "|" + DATE_REGEX + "|" +
                    NUMBER_REGEX + "|" + ABBREVIATION_REGEX + "|" +
                    CONTRACTION_REGEX + "|" + WORD_REGEX
    );

    @Override
    public List<String> tokenize(String text) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = TOKEN_PATTERN.matcher(text);

        while (matcher.find()) {
            String token = matcher.group();
            tokens.addAll(normalizeToken(token));
        }

        return tokens;
    }

    private List<String> normalizeToken(String token) {
        List<String> resultTokens = new ArrayList<>();
        token = token.toLowerCase();

        if (token.matches(".*\\b\\w+'\\w+\\b.*")) {
            token = token.replaceAll("n't\\b", " not");
            token = token.replaceAll("'re\\b", " are");
            token = token.replaceAll("'ll\\b", " will");
            token = token.replaceAll("'ve\\b", " have");
            token = token.replaceAll("'m\\b", " am");
            token = token.replaceAll("'d\\b", " would");

            if (token.contains(" ")) {
                String[] splitTokens = token.split("\\s+");
                Collections.addAll(resultTokens, splitTokens);
            } else {
                resultTokens.add(token);
            }
        } else if (token.matches(".*\\w+'s\\b")) {
            String baseWord = token.replaceAll("'s\\b", "");
            resultTokens.add(baseWord);
        } else {
            resultTokens.add(token);
        }

        return resultTokens;
    }
}
