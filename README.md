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

Parsed the provided JSON file using Python scripting.(CODE NAME ADD)

Extracted values from JSON and inserted them into the Recipefood table in the database.

Creating APIs idRecipeFood, Paged, and Search: ->

Initialized a Spring Boot project using Spring Initializr and added the dependencies Spring Web, Spring Data JPA, MySQL Driver.

Built APIs using @GetMapping to handle:

idRecipeFood API -> Retrieve recipe by ID.

Paged API -> Retrieve recipes with pagination (page, limit).

Search API -> Retrieve recipes based on filters (search criteria applied on table columns).

Integrated Swagger UI for a user-friendly API interface (By adding therequired dependencies in POM.XML)

Allowed testing of endpoints directly from Swagger.

curl commands to run in the terminal to check whether we get the same output or not.

Verified API responses both via cURL commands through terminal and directly by pasting the URL in a chrome browser.






