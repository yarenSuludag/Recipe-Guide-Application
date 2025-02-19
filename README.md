# Recipe Guide Application 🍽️

### **A Java-Based Recipe Management System with Ingredient Matching and Cost Estimation**

## **Table of Contents**
- [Overview](#overview)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Database Design](#database-design)
- [Application Interface](#application-interface)
- [Functionality](#functionality)
- [Sorting and Filtering](#sorting-and-filtering)
- [Missing Ingredients and Cost Calculation](#missing-ingredients-and-cost-calculation)
- [Algorithm Overview](#algorithm-overview)
- [Future Improvements](#future-improvements)
- [Screenshots](#screenshots)
- [Installation](#installation)


---

## **Overview**
The **Recipe Guide Application** is a Java-based desktop application that allows users to store and manage recipe details, including ingredients and cooking instructions. Additionally, it suggests recipes based on available ingredients, calculates missing ingredient costs, and provides sorting/filtering options based on cooking time and budget. This project aims to enhance user experience by offering an intuitive interface with **Java Swing**.

---

## **Features**
✅ **Add, Update, and Delete Recipes**  
✅ **Ingredient-Based Recipe Suggestions**  
✅ **Ingredient Inventory Management**  
✅ **Dynamic Search and Filtering by Time & Cost**  
✅ **Missing Ingredient Cost Calculation**  
✅ **User-Friendly GUI with Java Swing**  

---

## **Technologies Used**
- **Programming Language**: Java ☕  
- **GUI Framework**: Swing 🖥️  
- **Database**: MySQL (or embedded file-based storage) 🗄️  
- **Algorithm**: Dynamic Search, Sorting, HashTables 📊  

---

## **Database Design**
The database is designed with **three core tables** to efficiently manage recipes and their relationships with ingredients.

### **1. Recipes Table (`recipes`)**
Stores essential information about each recipe.
- **Columns**: `recipe_id` (Primary Key), `name`, `category`, `prep_time`, `instructions`

### **2. Ingredients Table (`ingredients`)**
Contains information about ingredients used across multiple recipes.
- **Columns**: `ingredient_id` (Primary Key), `name`, `unit_price`, `stock_quantity`

### **3. Recipe-Ingredient Relationship Table (`recipe_ingredients`)**
Establishes a **many-to-many** relationship between recipes and ingredients.
- **Columns**: `recipe_id` (Foreign Key), `ingredient_id` (Foreign Key), `quantity`

This relational model **ensures efficient data retrieval** and **prevents duplication** of ingredient records.

---

## **Application Interface**
The application interface is designed to be **intuitive and user-friendly** with essential functionalities.
- **Main Menu**: Displays the list of saved recipes.
- **Search & Filter Panel**: Allows users to find recipes based on **ingredients, cooking time, and cost**.
- **Recipe Details Page**: Shows complete recipe instructions, required ingredients, and missing item costs.

---

## **Functionality**
### **1️⃣ Adding, Updating, and Deleting Recipes**
- Users can input recipe details such as:
  - Name
  - Category (e.g., Main Dish, Dessert)
  - Cooking Time
  - Instructions
  - Ingredients (with quantities)
- Recipes can be **updated or deleted**, and changes are reflected in the database.

### **2️⃣ Recipe Suggestions Based on Available Ingredients**
- Users can select their **available ingredients**.
- The system **matches recipes** that can be cooked with the given ingredients.
- Recipes are ranked based on **match percentage**.

---

## **Sorting and Filtering**
- Recipes can be sorted by:
  - **Preparation Time** ⏳ (Fastest to Slowest)
  - **Cost** 💰 (Cheapest to Most Expensive)
- Users can filter by:
  - **Category**
  - **Available Ingredients**
  - **Time Constraints**

---

## **Missing Ingredients and Cost Calculation**
- If a recipe **requires ingredients** not in stock, they are listed separately.
- The **total cost of missing ingredients** is displayed.
- Fully available recipes are marked ✅ **green**, while incomplete recipes are marked ❌ **red**.

---

## **Algorithm Overview**
The **recipe recommendation system** uses:
1. **HashTables** for ingredient-availability mapping.
2. **Dynamic Search Algorithm** for efficient filtering.
3. **Sorting Algorithms** for ranking based on cooking time and budget.


### **Future Improvements**

🚀 **Online Recipe API Integration**  
   - Fetch and display recipes from external sources.  
   - Allow users to save their favorite online recipes into the local database.

📸 **Add Recipe Images**  
   - Enable users to upload images for each recipe.  
   - Store and display images within the GUI for better visual appeal.

🔍 **Advanced Filtering & Dietary Preferences**  
   - Implement filters for dietary restrictions (e.g., Vegan, Gluten-Free, Keto).  
   - Enhance ingredient-based filtering for better recipe recommendations.

📊 **More Comprehensive Cost Analysis**  
   - Provide a **detailed cost breakdown** for each recipe.  
   - Compare cost-effectiveness between different recipes and suggest alternatives.

### **Screenshots**

### Application Interface
- **Main Screen**  
  ![Recipe App Main Screen](images/app_interface_1.png)  

- **Recipe Details Page**  
  ![Recipe Details](images/app_interface_2.png)  



### **Installation**

  1. **Clone the Repository**  
   Open a terminal and run:
   ```sh
   git clone https://github.com/yourusername/recipe-guide-app.git
   cd recipe-guide-app




