package com.exam.recipe_demo.service.impl;

import com.exam.recipe_demo.model.Recipefood;
import com.exam.recipe_demo.repository.RecipeRepo;
import com.exam.recipe_demo.service.RecipeSer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;

@Service
public class recipeImpl implements RecipeSer {

    @Autowired
    private RecipeRepo repos;

//    public recipeImpl(RecipeRepo repos) {
//        this.repos = repos;
//    }

    @Override
    public Recipefood getUSRecipe(Integer idRecipeFood) {
        return repos.findById(idRecipeFood)
                .orElseThrow(() -> new RuntimeException("Recipe not found with id " + idRecipeFood));
    }

    @Override
    public List<Recipefood> getAllRecipes() {
        return repos.findAll();
    }

    @Override
    public Page<Recipefood> getRecipes(Pageable pageable) {
        return repos.findAll(pageable);
    }

    @Override
    public List<Recipefood> searchRecipes(String title, String cuisine, String calories, String rating, String totalTime) {
        List<Recipefood> allRecipes = repos.findAll(); // get all recipes from DB
        ObjectMapper mapper = new ObjectMapper();

        return allRecipes.stream().filter(recipe -> {
            try {
                // Filter by title
                if (title != null && !title.isEmpty() && (recipe.getTitle() == null || !recipe.getTitle().toLowerCase().contains(title.toLowerCase())))
                    return false;

                // Filter by cuisine
                if (cuisine != null && !cuisine.isEmpty() && (recipe.getCuisine() == null || !recipe.getCuisine().equalsIgnoreCase(cuisine)))
                    return false;

                // Filter by calories
                if (calories != null && !calories.isEmpty()) {
                    JsonNode nutrients = mapper.readTree(recipe.getNutrients());
                    int cal = Integer.parseInt(nutrients.get("calories").asText().replaceAll("[^0-9]", ""));
                    String op = calories.substring(0, 2);
                    int value = Integer.parseInt(calories.replaceAll("[^0-9]", ""));
                    switch(op) {
                        case "<=": if (!(cal <= value)) return false; break;
                        case ">=": if (!(cal >= value)) return false; break;
                        case ">": if (!(cal > value)) return false; break;
                        case "<": if (!(cal < value)) return false; break;
                        case "=": if (!(cal == value)) return false; break;
                    }
                }

                // Filter by rating
                if (rating != null && !rating.isEmpty()) {
                    double r = recipe.getRating() != null ? recipe.getRating() : 0;
                    String op = rating.substring(0, 2);
                    double value = Double.parseDouble(rating.replaceAll("[^0-9.]", ""));
                    switch(op) {
                        case "<=": if (!(r <= value)) return false; break;
                        case ">=": if (!(r >= value)) return false; break;
                        case ">": if (!(r > value)) return false; break;
                        case "<": if (!(r < value)) return false; break;
                        case "=": if (!(r == value)) return false; break;
                    }
                }

                // Filter by totalTime
                if (totalTime != null && !totalTime.isEmpty()) {
                    int t = recipe.getTotalTime() != null ? recipe.getTotalTime() : 0;
                    String op = totalTime.substring(0, 2);
                    int value = Integer.parseInt(totalTime.replaceAll("[^0-9]", ""));
                    switch(op) {
                        case "<=": if (!(t <= value)) return false; break;
                        case ">=": if (!(t >= value)) return false; break;
                        case ">": if (!(t > value)) return false; break;
                        case "<": if (!(t < value)) return false; break;
                        case "=": if (!(t == value)) return false; break;
                    }
                }

            } catch (Exception e) {
                return false; // skip if parsing fails
            }

            return true;
        }).toList();
    }


}
