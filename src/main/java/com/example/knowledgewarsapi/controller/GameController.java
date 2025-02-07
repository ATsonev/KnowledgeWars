package com.example.knowledgewarsapi.controller;

import com.example.knowledgewarsapi.service.QuestionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class GameController {

    private final QuestionService questionService;

    public GameController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/categories")
    public List<String> getCategories() {
        return questionService.getCategories();
    }

/*    @GetMapping("/question")
    public Question getQuestion(@RequestParam String category) {
        return questionService.getQuestionByCategory(category);
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitAnswer(@RequestBody AnswerSubmission submission) {
        boolean isCorrect = questionService.checkAnswer(submission);
        return ResponseEntity.ok(isCorrect ? "Correct!" : "Wrong!");
    }*/
}
