# Recipe Data Collection and API

This project demonstrates how to parse recipe JSON data, store it in a MySQL database, and expose RESTful APIs to connect with a frontend application.

Objective 1: Establish a new connection in MySQL Workbench and create a recipes database.

Objective 2: Create a Recipefood table with the required columns.

Objective 3: Insert values by parsing JSON data using Python scripting and store them in the database.

Objective 4: Build APIs idRecipeFood, paged, and search to access the data and connect them to a frontend by calling APIs.

Implementation Steps: ->

Firstly created a new MySQL connection in MySQL Workbench.

Connection settings:

Host Name: 127.0.0.1

Port: 3306

Username: root

Creating a Table and Columns: ->

Here I have created a new database named as "recipes".

Inside this database I created a table "Recipefood" with appropriate columns.

Inserting JSON Data into the Table: ->

Connected MySQL Workbench with vscode using the required connector installed via pip install.

Parsed the provided JSON file using Python scripting code "json_parse.py".

Extracted values from JSON and inserted them into the Recipefood table in the database.

Creating APIs idRecipeFood, Paged, and Search: ->

Initialized a Spring Boot project using Spring Initializr and added the dependencies Spring Web, Spring Data JPA, MySQL Driver.

Built APIs using @GetMapping to handle:

idRecipeFood API -> Retrieve recipe by ID.

sql command:

select * from Recipefood where idRecipeFood = 102;

curl command:

curl -X 'GET' \
  'http://localhost:8080/api/recipes/paged?page=1&limit=10&sortBy=rating' \
  -H 'accept: /'

URL:

http://localhost:8080/api/recipes/1

Paged API -> Retrieve recipes with pagination (page, limit).

sql command:

SELECT * 
FROM Recipefood
ORDER BY rating DESC
LIMIT 10 OFFSET 0;  -- page 1, limit 5 => offset = (1-1)*5

curl command:

curl -X 'GET' \
  'http://localhost:8080/api/recipes/paged?page=1&limit=10&sortBy=rating' \
  -H 'accept: /'

URL:

[http://localhost:8080/api/recipes/1](http://localhost:8080/api/recipes/paged?page=1&limit=10&sortBy=rating)

Search API -> Retrieve recipes based on filters (search criteria applied on table columns).

sql command:

SELECT *
FROM Recipefood
WHERE 
    title LIKE '%pie%'                -- partial match for title
    AND rating >= 4.5                 -- rating >= 4.5
    AND CAST(JSON_UNQUOTE(JSON_EXTRACT(nutrients, '$.calories')) AS UNSIGNED) <= 400;

curl command:

curl -X 'GET' \
  'http://localhost:8080/api/recipes/search?title=pie&calories=%3C%3D400&rating=%3E%3D4.5' \
  -H 'accept: /'

URL:

[http://localhost:8080/api/recipes/1](http://localhost:8080/api/recipes/search?title=pie&calories=%3C%3D400&rating=%3E%3D4.5)

Integrated Swagger UI for a user-friendly API interface (By adding the required dependencies in POM.XML).

Allowed testing of endpoints directly from Swagger.

curl commands to run in the terminal to check whether we get the same output or not.

Verified API responses both via cURL commands through terminal and directly by pasting the URL in a chrome browser.






