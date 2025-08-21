package com.exam.recipe_demo.controller;


import com.exam.recipe_demo.model.Recipefood;
import com.exam.recipe_demo.service.RecipeSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipes")
public class recipeAPI {


    private  final RecipeSer recipeSer;

    public recipeAPI(RecipeSer recipeSer) {
        this.recipeSer = recipeSer;
    }

    @GetMapping("/{idRecipeFood}")
    public Recipefood getRecipes(@PathVariable("idRecipeFood") Integer idRecipeFood) {

        return recipeSer.getUSRecipe(idRecipeFood);

    }

    @GetMapping()
    public List<Recipefood> getAllRecipes() {

        return recipeSer.getAllRecipes();
    }

    @GetMapping("/paged")
    public Map<String, Object> getAllRecipesPaged(
            @RequestParam(defaultValue = "1") int page,       // page starts from 1
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "rating") String sortBy  // sort by rating descending
    ) {
        // Spring PageRequest is 0-based, so subtract 1
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(sortBy).descending());
        Page<Recipefood> recipePage = recipeSer.getRecipes(pageable);

        // Build custom response
        Map<String, Object> response = new HashMap<>();
        response.put("page", page);
        response.put("limit", limit);
        response.put("total", recipePage.getTotalElements());
        response.put("data", recipePage.getContent());

        return response;
    }

    @GetMapping("/search")
    public List<Recipefood> searchRecipes(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String cuisine,
            @RequestParam(required = false) String calories,   // e.g., "<=400"
            @RequestParam(required = false) String rating,     // e.g., ">=4.5"
            @RequestParam(required = false) String totalTime   // e.g., "<=60"
    ) {
        return recipeSer.searchRecipes(title, cuisine, calories, rating, totalTime);
    }

}
