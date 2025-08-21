package com.exam.recipe_demo.repository;

import com.exam.recipe_demo.model.Recipefood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;


@Repository
public interface RecipeRepo extends JpaRepository<Recipefood,Integer>, JpaSpecificationExecutor<Recipefood> {

}
