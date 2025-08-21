json data extraction:

Data Cleaning:->

Function clean_value(v):

Converts invalid numerical values such as NaN or Python None into SQL-compatible NULL.

Prevents MySQL errors when inserting missing or corrupted values.

Function safe_json(v):

Serializes Python dictionaries (like nutrients) into valid JSON strings using json.dumps.

Handling NaN Values:

When parsing the JSON file and inserting records into the MySQL database, certain numeric fields may contain invalid values represented as NaN (Not a Number). 

These cannot be stored directly in MySQL, as they will cause errors.

To avoid these errors I have detected NaN values in numeric fields before insertion.

Replace them with NULL so the database remains consistent.

In Python, math.isnan() is used to check if a value is NaN.

def clean_value(v):
    """Convert NaN/None to None for MySQL insertion"""
    if v is None:            # Handle empty values
        return None
    if isinstance(v, float) and math.isnan(v):  # Check for NaN
        return None
    return v

Database Connectivity:

Uses mysql.connector to establish a connection with the recipes database.

Parameters include:

Host: 127.0.0.1

User: root

Password: (default empty, configurable)

Database: recipes

JSON Parsing:

Reads US_recipes.json from the filesystem.

Converts the JSON into a Python dictionary.

Each recipe record is extracted as a dictionary with keys such as:

cuisine, title, rating, prep_time, cook_time, total_time, description, nutrients, serves.

Data Insertion:

Iterates through each recipe object.

Executes parameterized INSERT queries into the Recipefood table:

INSERT INTO Recipefood (
  cuisine, title, rating, prep_time, cook_time,
  total_time, description, nutrients, serves
) VALUES (?,?,?,?,?,?,?,?,?);

Uses prepared statements (%s) to prevent SQL injection and ensure safe insertion.

Transaction Handling:

After batch insertion, conn.commit() finalizes changes.

Cursor and connection are explicitly closed to free resources.

---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

Code:->
    
import json
import math
import mysql.connector

def clean_value(v):
    """Convert NaN/None to None for MySQL insertion"""
    if v is None:
        return None
    if isinstance(v, float) and math.isnan(v):
        return None
    return v

def safe_json(v):
    """Dump dict safely into valid JSON string for MySQL"""
    if not v:
        return None
    try:
        return json.dumps(v, allow_nan=False)
    except (TypeError, ValueError):
        return None

# Connect to MySQL
conn = mysql.connector.connect(
    host="127.0.0.1",
    user="root",
    password="",
    database="recipes"
)
cursor = conn.cursor()

# Load JSON file
with open("/Users/srinidhinunna/Desktop/Deepika Pro/US_recipes.json", "r") as f:
    data = json.load(f)

# Insert data into MySQL (without idRecipefood)
for _, item in data.items():
    cursor.execute("""
        INSERT INTO Recipefood (
            cuisine, title, rating,
            prep_time, cook_time, total_time, description,
            nutrients, serves
        )
        VALUES (%s,%s,%s,%s,%s,%s,%s,%s,%s)
    """, (
        clean_value(item.get("cuisine")),
        clean_value(item.get("title")),
        clean_value(item.get("rating")),
        clean_value(item.get("prep_time")),
        clean_value(item.get("cook_time")),
        clean_value(item.get("total_time")),
        clean_value(item.get("description")),
        safe_json(item.get("nutrients")),
        clean_value(item.get("serves"))
    ))

conn.commit()
cursor.close()
conn.close()
