# Karori 2022/2023
# 🍽️ FitApp - Mobile Fit Application

## 📱 Project Overview

**Karori** is a mobile application developed as an academic project within the context of Mobile Application Development. The app aims to provide a user-friendly platform for searching, viewing, and count the kilocalories , leveraging external APIs for data retrieval and backend services for authentication and data persistence.

## 🧩 Project Design Phases

### 1. Requirements Analysis and Use Cases

During the initial phase, we conducted a thorough requirements analysis, leading to the definition of **use cases**. Specifically, we identified various **actors** (User, System, External Services) and their interactions, clearly outlining all **«include» relationships** to represent shared behaviors among use cases.

![Use Case](images/Use%20case.png)

We started from a conceptual idea, which served as the foundation for the design and functional development of the application.
The following three images represent the initial prototype, which guided the implementation of the core features and user experience of the app.

<p align="center">
  <img src="images/How it started.png" alt="Prototype 1" width="30%" style="margin-right: 10px;">
  <img src="images/How it started 2.png" alt="Prototype 2" width="30%" style="margin-right: 10px;">
  <img src="images/How it started 3.png" alt="Prototype 3" width="30%">
</p>

### 2. System Architecture

The application was designed following a modular and scalable architecture based on:

- **Activities and Fragments**: for managing the user interface and screen navigation.
- **ViewModel**: for handling UI-related logic and state independently from the UI lifecycle.
- **Repository**: to abstract the data sources (remote/local) and enforce separation of concerns.

### 3. MVVM Architectural Pattern

The app follows the **MVVM (Model-View-ViewModel)** pattern to ensure a clear separation of responsibilities and simplify testing. This approach enables effective state management and responsive UI updates.

### 4. Components and Utilities Used

- **Custom Listeners**: for managing events and callbacks between components.
- **Adapters**: for binding data to RecyclerViews.
- **Factory Classes**: for controlled instantiation and initialization of ViewModels.

### 5. Integration with External Services

The app integrates several external APIs and backend services, including:

- **Spoonacular API**: for retrieving dynamic content such as recipes, ingredients, and nutritional information.
- **Firebase (Authentication & Firestore)**: for user management, authentication, and storage of saved recipes.

## 🖼️ Main Screens Overview

### 🔐 **Login / Registration**
The user enters their credentials—email and password—or logs in via their Google account, unless they have previously authenticated.
If a login was already performed in a past session, the system automatically logs the user in by loading the previously stored session data.

The user can log out at any time through the “Settings” section of the application. Logging out will clear all information related to automatic login, effectively resetting the authentication state.

![Login Page](images/login%20utente.png)

### 🍽️ **Home (Diet-Friendly Recipes)**
This screen displays a daily summary divided into three main meals: Breakfast, Lunch, and Dinner.
Users can switch between these meal sections by swiping left or right across the screen.

The “See more” button allows users to view the specific foods that contributed to the nutritional values shown on the screen.
Upon clicking, a pop-up window appears listing the consumed items. By selecting an individual food item, the app navigates to a detailed view that provides more specific nutritional data.

The “Add” button enables users to search for a food item they have consumed and add it to one of the three respective meal lists (Breakfast, Lunch, or Dinner).

At the top of the screen, the application logo dynamically displays the total calories consumed, calculated according to a previously defined formula.

<p align="center">
  <img src="images/Home screen" alt="Daily Summary 1" width="45%" style="margin-right: 10px;">
  <img src="images/Riassunto" alt="Daily Summary 2" width="45%">
</p>


### 🔍 **Recipe Search**
Enables users to search for recipes using keywords. Results can be filtered by diet type, preparation time, and number of ingredients.

### 📖 **Recipe Details**
Shows a detailed view including ingredients, preparation steps, nutritional values, and an option to save the recipe.

### ❤️ **Saved Recipes**
Lets users view and manage the recipes they have previously saved to their profile.

### ⚙️ **User Profile**
A personal area where the user can manage account details and access their collection of saved recipes.

## 🔧 Technologies Used

- **Language**: Kotlin  
- **Framework**: Android Jetpack (LiveData, ViewModel, Navigation)  
- **Libraries**: Retrofit, Glide, Firebase SDK, Material Design Components  
- **Architecture**: MVVM  
- **Backend**: Firebase Firestore  
- **External API**: Spoonacular API  

## 🚀 Installation Instructions

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/foodapp.git
   
Gruppo: MELMA <br />
Componenti Gruppo: <br />
Monti Lorenzo 869960 <br />
Oltolini Edoardo 869124 <br />
Gherardi Marco 869138 <br />
Lombardo Matteo 869232

