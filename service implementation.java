recipelmpl.java:->

Service Layer: recipeImpl.java

This class implements the RecipeSer service interface and contains the business logic for retrieving, paginating, and searching recipes from the database.

It interacts with the repository (RecipeRepo) to fetch data from MySQL and applies filtering logic for search queries.

1. Annotations
@Service
public class recipeImpl implements RecipeSer {


@Service: Marks this class as a Spring-managed service component.

Implements the RecipeSer interface, ensuring separation of service logic from controller and repository layers.

2. Dependency Injection
@Autowired
private RecipeRepo repos;


Injects the RecipeRepo JPA repository, which provides database access methods.

This avoids writing raw SQL; instead, repos uses Spring Data JPA for CRUD operations.

3. Fetch Recipe by ID
@Override
public Recipefood getUSRecipe(Integer idRecipeFood) {
    return repos.findById(idRecipeFood)
        .orElseThrow(() -> new RuntimeException("Recipe not found with id " + idRecipeFood));
}


Calls repos.findById() to fetch a recipe by primary key.

If not found, throws a runtime exception.

Ensures the API returns a clear error if an invalid ID is requested.

4. Fetch All Recipes
@Override
public List<Recipefood> getAllRecipes() {
    return repos.findAll();
}


Returns all recipes from the database using JPA.

Useful for testing or smaller datasets.

5. Paginated Recipes
@Override
public Page<Recipefood> getRecipes(Pageable pageable) {
    return repos.findAll(pageable);
}


Uses Spring Data’s Pageable object to apply pagination and sorting.

Supports efficient queries for large datasets.

This method directly integrates with the controller’s /paged API.

6. Search Recipes with Filters
@Override
public List<Recipefood> searchRecipes(String title, String cuisine, String calories, String rating, String totalTime) {
    List<Recipefood> allRecipes = repos.findAll(); // fetch everything first
    ObjectMapper mapper = new ObjectMapper();
    
    return allRecipes.stream().filter(recipe -> {
        try {
            ...
        } catch (Exception e) {
            return false; // skip recipe if parsing fails
        }
        return true;
    }).toList();
}


This is the core of search logic. Instead of database-level queries, it loads all recipes and applies in-memory filtering with Java Streams.

🔎 Filters Applied:

Title filter

if (title != null && !title.isEmpty() && (recipe.getTitle() == null 
     || !recipe.getTitle().toLowerCase().contains(title.toLowerCase())))
    return false;


Case-insensitive substring match.

Example: title=pie → matches “Apple Pie”, “Pumpkin Pie”.

Cuisine filter

if (cuisine != null && !cuisine.isEmpty() 
     && (recipe.getCuisine() == null || !recipe.getCuisine().equalsIgnoreCase(cuisine)))
    return false;


Exact match (ignores case).

Example: cuisine=Italian.

Calories filter

JsonNode nutrients = mapper.readTree(recipe.getNutrients());
int cal = Integer.parseInt(nutrients.get("calories").asText().replaceAll("[^0-9]", ""));
String op = calories.substring(0, 2);
int value = Integer.parseInt(calories.replaceAll("[^0-9]", ""));


Extracts calories from the nutrients JSON column.

Supports operators (<=, >=, <, >, =).

Example: calories=<=400 → keeps recipes ≤ 400 calories.

Rating filter

double r = recipe.getRating() != null ? recipe.getRating() : 0;


Checks if rating exists, else defaults to 0.

Supports operators like >=4.5.

Total Time filter

int t = recipe.getTotalTime() != null ? recipe.getTotalTime() : 0;


Filters by total preparation time.

Example: totalTime=<=60 → recipes taking ≤ 60 minutes.

7. Error Handling
catch (Exception e) {
    return false; // skip if parsing fails
}


If parsing nutrients JSON or numeric values fails, that recipe is excluded from results.

Prevents runtime crashes due to malformed data.

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Recipeser.java:->

Service Layer Interface: RecipeSer.java

The RecipeSer interface defines the contract (or blueprint) for the recipe service in your Spring Boot application.

It sits between the Controller Layer (which handles HTTP requests) and the Repository Layer (which talks to the database).

By using an interface, you decouple the API layer from the implementation, making the application flexible and easier to maintain.

Technical Details
1. Interface Definition
public interface RecipeSer {


Declares a service interface for recipe operations.

Any class (like recipeImpl) that implements this interface must provide implementations for all declared methods.

Promotes abstraction and loose coupling.

2. Get Recipe by ID
Recipefood getUSRecipe(Integer idRecipeFood);


Input: idRecipeFood (Primary key of recipe).

Output: Recipefood entity.

Purpose: Fetch a single recipe by its unique identifier.

Used by controller’s endpoint:

GET /api/recipes/{idRecipeFood}

3. Get All Recipes
List<Recipefood> getAllRecipes();


Returns a list of all recipes in the database.

Simple retrieval with no pagination.

Useful for testing, small datasets, or backend admin tools.

4. Get Recipes (Paginated)
Page<Recipefood> getRecipes(Pageable pageable);


Uses Spring Data Pageable to return paginated and sorted results.

Example:

Page 1, 10 items per page, sorted by rating.

Directly supports large datasets without overloading the client.

Used by controller endpoint:

GET /api/recipes/paged?page=1&limit=10&sortBy=rating

5. Search Recipes with Filters
List<Recipefood> searchRecipes(String title, String cuisine, String calories, String rating, String totalTime);


Accepts multiple optional filters:

title → partial text match.

cuisine → exact match.

calories → numeric filter (<=400, >=200, etc.).

rating → numeric filter (>=4.5, etc.).

totalTime → numeric filter (<=60 minutes, etc.).

Returns all recipes that match the filters.

Enables flexible querying for clients without writing multiple endpoints.

Connected to controller endpoint:

GET /api/recipes/search?title=pie&cuisine=American&calories=<=400&rating=>=4.5


-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------


recipedemoapplication.java:->

Main Application Entry Point: RecipeDemoApplication.java

This file serves as the starting point of the Spring Boot application.

Technical Explanation
1. Class Annotation
@SpringBootApplication


This is a convenience annotation that combines:

@Configuration → Marks the class as a source of Spring bean definitions.

@EnableAutoConfiguration → Automatically configures Spring Boot based on dependencies in the classpath (e.g., configures Spring Data JPA when it detects Hibernate & MySQL driver).

@ComponentScan → Automatically scans for Spring components (@Service, @Repository, @Controller) in the current package and sub-packages.

➡️ In your case, it auto-detects:

recipeAPI (Controller)

recipeImpl (Service)

RecipeRepo (Repository)

Recipefood (Entity)

2. Main Method
public static void main(String[] args) {
    SpringApplication.run(RecipeDemoApplication.class, args);
}


Entry point of the application.

SpringApplication.run(...): Bootstraps the Spring Boot app by:

Starting the embedded Tomcat server (default).

Creating the Spring Application Context (IoC container).

Performing auto-configuration (like setting up JPA, DataSource, Hibernate with MySQL).

Launching the web application, so your REST APIs become available.

3. Execution Flow

Run the class (RecipeDemoApplication) → starts Spring Boot.

Spring scans for annotated components and wires dependencies.

Database connection is established (via application.properties).

REST APIs (recipeAPI) are exposed at:

http://localhost:8080/api/recipes
