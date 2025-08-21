package com.exam.recipe_demo.model;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "Recipefood")
@Data
//@NoArgsConstructor
//@AllArgsConstructor

public class Recipefood {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // matches AUTO_INCREMENT
    @Column(name = "idRecipefood")
    private Integer idRecipeFood;

    @Column(name = "cuisine")
    private String cuisine;

    @Column(name = "title")
    private String title;

    @Column(name = "rating")
    private Float rating;

    @Column(name = "prep_time")
    private Integer prepTime;

    @Column(name = "cook_time")
    private Integer cookTime;

    @Column(name = "total_time")
    private Integer totalTime;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "nutrients", columnDefinition = "JSON")
    private String nutrients;

    @Column(name = "serves")
    private String serves;

    public Recipefood() {
    }

    public Recipefood(Integer idRecipeFood, String cuisine, String title, Float rating, Integer prepTime, Integer cookTime, Integer totalTime, String description, String nutrients, String serves) {
        this.idRecipeFood = idRecipeFood;
        this.cuisine = cuisine;
        this.title = title;
        this.rating = rating;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.totalTime = totalTime;
        this.description = description;
        this.nutrients = nutrients;
        this.serves = serves;
    }


    public Integer getIdRecipeFood() {
        return idRecipeFood;
    }

    public void setIdRecipeFood(Integer idRecipeFood) {
        this.idRecipeFood = idRecipeFood;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(Integer prepTime) {
        this.prepTime = prepTime;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public void setCookTime(Integer cookTime) {
        this.cookTime = cookTime;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNutrients() {
        return nutrients;
    }

    public void setNutrients(String nutrients) {
        this.nutrients = nutrients;
    }

    public String getServes() {
        return serves;
    }

    public void setServes(String serves) {
        this.serves = serves;
    }
}


