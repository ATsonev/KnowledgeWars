package com.example.knowledgewarsapi.controller;

import com.example.knowledgewarsapi.model.Question;
import com.example.knowledgewarsapi.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class GameController {

    private final QuestionService questionService;

    public GameController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("/categories")
    public String categories(Model model, HttpSession session) {
        List<String> categories = questionService.getCategories();
        model.addAttribute("categories", categories);

        Integer currentPlayer = (Integer) session.getAttribute("currentPlayer");
        if (currentPlayer == null) {
            currentPlayer = 1;
            session.setAttribute("currentPlayer", currentPlayer);
        }
        model.addAttribute("currentPlayer", currentPlayer);

        return "categories";
    }

    @GetMapping("/answer-question")
    public String getQuestion(@RequestParam String category, Model model) {
        Question question = questionService.getQuestion(category);
        model.addAttribute("question", question);
        return "question";
    }

    @PostMapping("/submit-answer")
    public String submitAnswer(@RequestParam String selectedAnswer,
                               @RequestParam String correctAnswer,
                               @RequestParam String category,
                               HttpSession session) {
        Integer currentPlayer = (Integer) session.getAttribute("currentPlayer");
        if (currentPlayer == null) {
            currentPlayer = 1;
        }

        Integer player1Score = (Integer) session.getAttribute("player1Score");
        Integer player2Score = (Integer) session.getAttribute("player2Score");

        if (player1Score == null) player1Score = 0;
        if (player2Score == null) player2Score = 0;

        if (selectedAnswer.trim().equalsIgnoreCase(correctAnswer.trim())) {
            if (currentPlayer == 1) {
                player1Score += 300;
            } else {
                player2Score += 300;
            }
        }

        currentPlayer = (currentPlayer == 1) ? 2 : 1;

        session.setAttribute("currentPlayer", currentPlayer);
        session.setAttribute("player1Score", player1Score);
        session.setAttribute("player2Score", player2Score);

        Set<String> disabledCategories = (Set<String>) session.getAttribute("disabledCategories");
        if (disabledCategories == null) {
            disabledCategories = new HashSet<>();
        }
        disabledCategories.add(category);
        session.setAttribute("disabledCategories", disabledCategories);

        return "redirect:/categories";
    }
}
