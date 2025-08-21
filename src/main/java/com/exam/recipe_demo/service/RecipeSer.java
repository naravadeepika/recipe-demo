package com.exam.recipe_demo.service;

import com.exam.recipe_demo.model.Recipefood;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RecipeSer {
     Recipefood getUSRecipe(Integer idRecipeFood );
     List<Recipefood> getAllRecipes();
    Page<Recipefood> getRecipes(Pageable pageable);
    List<Recipefood> searchRecipes(String title, String cuisine, String calories, String rating, String totalTime);

}
