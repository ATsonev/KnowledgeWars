package com.example.knowledgewarsapi.controller;

import com.example.knowledgewarsapi.model.Question;
import com.example.knowledgewarsapi.service.QuestionService;
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
    public String categories(Model model) {
        List<String> categories = questionService.getCategories();
        model.addAttribute("categories", categories);
        return "categories";
    }

    /*@GetMapping("/get-categories")
    public String getCategories(){
        questionService.getCategories();
        return null;
    }

    @GetMapping("/get-question")
    public String getQuestion(@RequestParam String category){
        Question question = questionService.getQuestion(category);
        return null;
    }*/
}
