package com.example.knowledgewarsapi.service;

import com.example.knowledgewarsapi.model.Question;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
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
        String prompt = "Generate a fresh list of **10 trivia categories** with **short, engaging names (1-3 words max)**. " +
                "Make sure the names are **not too long or overly detailed**. " +
                "The categories should resemble those in classic quiz shows, such as 'World Inventions', 'Famous Musicians', 'News', 'Renaissance'. " +
                "Avoid single-word categories like 'History' or 'Science', but also do **not** make them unnecessarily long. " +
                "Ensure the categories cover **a variety of topics**, including history, science, music, geography, literature" +
                ",geography, technology and interesting facts. Make sure **each request generates different categories** to keep the game dynamic. " +
                "Return only the categories as a numbered list, without explanations or extra text.\\n" +
                "Example format:\\n" +
                "1. Hidden Wonders\\n" +
                "2. Inventors & Inventions\\n" +
                "3. Unsolved Mysteries\\n" +
                "4. Space Discoveries\\n" +
                "5. Great Battles\\n" +
                "6. Movie Legends\\n" +
                "7. Myth & Legends\\n" +
                "8. Famous Speeches\\n" +
                "9. Science Wonders\\n" +
                "10. Landmarks of the World";

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
                categories.add(cleanedCategory);
            }
        }

        System.out.println(categories);

        return categories;
    }

    public Question getQuestion(String category) {
        String prompt = "Generate a **challenging** trivia question for the category '" + category + "'. " +
                "The question should be **medium to hard difficulty**, requiring actual knowledge instead of being obvious. " +
                "Ensure the question is unique and not a commonly repeated one. " +
                "Provide exactly **3 possible answers (A, B, and C)**, with only one being correct. " +
                "Mix different question types (e.g., historical facts, rare knowledge, numbers, or logical reasoning). " +
                "Ensure the format is **valid JSON** and does not include extra text. " +
                "Use this format:\n" +
                "{\n" +
                "  \"question\": \"A difficult trivia question related to '" + category + "'\",\n" +
                "  \"options\": {\n" +
                "    \"A\": \"Option 1\",\n" +
                "    \"B\": \"Option 2\",\n" +
                "    \"C\": \"Option 3\"\n" +
                "  },\n" +
                "  \"correct\": \"A\"\n" +
                "}";

        JSONObject requestJson = new JSONObject();
        requestJson.put("model", "mistral");
        requestJson.put("prompt", prompt);
        requestJson.put("stream", false);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestJson.toString(), headers);
        ResponseEntity<String> response = restTemplate.postForEntity(OLLAMAURL, request, String.class);

        String responseBody = response.getBody();

        JSONObject obj = new JSONObject(responseBody);
        String jsonResponseStr = obj.getString("response").trim();
        JSONObject jsonResponse = new JSONObject(jsonResponseStr);

        String question = jsonResponse.getString("question");
        JSONObject optionsObj = jsonResponse.getJSONObject("options");
        List<String> options = Arrays.asList(
                optionsObj.getString("A"),
                optionsObj.getString("B"),
                optionsObj.getString("C")
        );
        String correctAnswer = jsonResponse.getString("correct");

        return new Question(question, options, correctAnswer);
    }
}
