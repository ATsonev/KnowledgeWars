package com.example.knowledgewarsapi.controller;

import com.example.knowledgewarsapi.model.Question;
import com.example.knowledgewarsapi.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
