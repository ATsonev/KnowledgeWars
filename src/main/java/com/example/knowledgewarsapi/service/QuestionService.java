package com.example.knowledgewarsapi.service;

import com.example.knowledgewarsapi.model.Question;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class QuestionService {
    String OLLAMAURL = "http://localhost:11434/api/generate";
    public String category;
    private final RestTemplate restTemplate;

    public QuestionService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> getCategories() {


        String prompt = "Generate 12 trivia categories. Each name must be **ONE SINGLE WORD** only.  " +
                "No explanations, no descriptions. Just return the names in a numbered list";

        String requestBody = "{ \"model\": \"mistral\", \"prompt\": \"" + prompt + "\", \"stream\": false }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(OLLAMAURL, request, String.class);

        String responseBody = response.getBody();

        JSONObject obj = new JSONObject(responseBody);
        String responseC = obj.getString("response");

        String[] lines = responseC.split("\n");

        List<String> categories = new ArrayList<>();

        Pattern pattern = Pattern.compile("^\\d+\\.\\s*");

        for (String line : lines) {
            line = line.trim();
            Matcher matcher = pattern.matcher(line);
            String cleanedCategory = matcher.replaceFirst("");
            if (!cleanedCategory.isEmpty()) {
                category = cleanedCategory;
            }
        }

        System.out.println(categories);

        return categories;
    }

    public Question getQuestion(String category){
        String prompt = "Generate a trivia question for " + category +
                " with exactly 4 possible answers.\\n" +
                "Only 1 answer should be correct. Strictly follow this response format, do not add any extra text:\\n" +
                "Question: [Your question]\\n" +
                "A) [Option 1]\\n" +
                "B) [Option 2]\\n" +
                "C) [Option 3]\\n" +
                "D) [Option 4]\\n" +
                "Correct: [Just the correct option letter, example: A]";

        String requestBody = "{ \"model\": \"mistral\", \"prompt\": \"" + prompt + "\", \"stream\": false }";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(OLLAMAURL, request, String.class);

        String responseBody = response.getBody();
        //TODO return Question entity
        return null;
    }
}
