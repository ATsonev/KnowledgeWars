package com.example.knowledgewarsapi.controller;

import com.example.knowledgewarsapi.model.Question;
import com.example.knowledgewarsapi.service.QuestionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Controller
public class GameController {

    private final QuestionService questionService;

    public GameController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping("")
    public String index(HttpSession session){
        session.invalidate();
        return "index";
    }

    @GetMapping("/categories")
    public String categories(Model model, HttpSession session) {
        if (session.getAttribute("categories") == null) {
            List<String> categories = questionService.getCategories();
            session.setAttribute("categories", categories);
            session.setAttribute("disabledCategories", new HashSet<String>());
            model.addAttribute("categories", categories);
        } else {
            model.addAttribute("categories", session.getAttribute("categories"));
        }

        if (session.getAttribute("player1Name") == null || session.getAttribute("player2Name") == null) {
            session.setAttribute("player1Name", "Player 1");
            session.setAttribute("player2Name", "Player 2");
            session.setAttribute("player1Score", 0);
            session.setAttribute("player2Score", 0);
        }

        Integer currentPlayer = (Integer) session.getAttribute("currentPlayer");
        if (currentPlayer == null) {
            currentPlayer = 1;
            session.setAttribute("currentPlayer", currentPlayer);
        }
        model.addAttribute("currentPlayer", currentPlayer);
        model.addAttribute("player1Name", session.getAttribute("player1Name"));
        model.addAttribute("player2Name", session.getAttribute("player2Name"));
        model.addAttribute("player1Score", session.getAttribute("player1Score"));
        model.addAttribute("player2Score", session.getAttribute("player2Score"));
        model.addAttribute("currentPlayer", currentPlayer);
        model.addAttribute("disabledCategories", session.getAttribute("disabledCategories"));

        return "categories";
    }

    @GetMapping("/answer-question")
    public String getQuestion(@RequestParam String category, Model model) {
        Question question = questionService.getQuestion(category);
        model.addAttribute("question", question);
        model.addAttribute("category", category);
        return "question";
    }

    @PostMapping("/submit-answer")
    public String submitAnswer(@RequestParam String selectedAnswer,
                               @RequestParam String category,
                               @RequestParam String correctAnswer,
                               HttpSession session) {
        Integer currentPlayer = (Integer) session.getAttribute("currentPlayer");
        if (selectedAnswer.trim().equalsIgnoreCase(correctAnswer)) {
            if (currentPlayer == 1) {
                Integer player1Score = (Integer) session.getAttribute("player1Score");
                session.setAttribute("player1Score", player1Score + 300);
            } else {
                Integer player2Score = (Integer) session.getAttribute("player2Score");
                session.setAttribute("player2Score", player2Score + 300);
            }
        }

        Set<String> disabledCategories = (Set<String>) session.getAttribute("disabledCategories");
        disabledCategories.add(category);
        session.setAttribute("disabledCategories", disabledCategories);

        if (currentPlayer == 1) {
            session.setAttribute("currentPlayer", 2);
        } else {
            session.setAttribute("currentPlayer", 1);
        }

        return "redirect:/categories";
    }

}
