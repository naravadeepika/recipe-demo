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
