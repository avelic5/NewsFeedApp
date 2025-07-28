# NewsFeedApp

**NewsFeedApp** is an advanced Android application built with **Kotlin** and **Jetpack Compose**, designed for browsing, filtering, and viewing news articles. The app features multiple news layouts, advanced filtering options, integration with web services, and local persistence using Room database.

---

## 🚀 Key Features

- Display a list of news articles with **two distinct layouts** based on the news type:  
  - **FeaturedNewsCard** for featured articles  
  - **StandardNewsCard** for standard articles

- Filter news by categories (`Politics`, `Sports`, `Science/Technology`, and `All`), including a “More filters...” chip that navigates to an advanced filtering screen.

- Advanced filtering screen includes:  
  - Category selection via Chips  
  - Date range filtering using Material 3 **DateRangePicker**  
  - Filtering out unwanted words with input field and management list

- Detailed news screen displaying:  
  - Title, snippet, category, source, and published date  
  - Related news from the same category based on closest published date and lexical ordering  
  - Navigation between related news articles and back to the main feed

- Efficient, lazy-loading list using **LazyColumn** for smooth scrolling and performance.

- Integration with remote web services for fetching:  
  - Top stories by category  
  - Similar news articles  
  - Image tagging service

- Local persistence of news and tags using **Room database** with relational mapping to handle many-to-many relationships between news and tags.

- Smart caching and update mechanisms:  
  - Avoid duplicate news entries  
  - Mark articles as featured or standard depending on the data source  
  - Cache API results and avoid redundant web service calls within 30 seconds

---

## 📦 Project Structure

- **model**: Data classes including `NewsItem` with all necessary fields like `uuid`, title, snippet, category, image tags, etc.  
- **data**: Data sources and repositories including `NewsDAO`, `ImageDAO`, and local persistence DAOs.  
- **screen**: Composable screens such as `NewsFeedScreen`, `FilterScreen`, and `NewsDetailsScreen` with navigation routes.  
- **ui**: UI components and composable functions for news cards and UI elements.  
- **MainActivity**: Launches the app and hosts the navigation graph.

---

## 📱 Screens Overview

### NewsFeedScreen (`/home`)

- Displays a filter chip group on top with categories and a "More filters..." option.  
- Shows a lazy-loaded list of news articles filtered according to the selected category or advanced filters.  
- Uses `StandardNewsCard` and `FeaturedNewsCard` composables depending on the article type.  
- Shows a message card when no news matches the filter.

### FilterScreen (`/filters`)

- Allows category selection with chips.  
- Provides a date range picker to filter articles by published date.  
- Text input for adding unwanted words to filter out.  
- "Apply filters" button applies the selection and returns to the main screen.  
- Back navigation discards filter changes.

### NewsDetailsScreen (`/details/{id}`)

- Shows all detailed information about the selected news article.  
- Displays tags retrieved from the image tagging service.  
- Lists two related news articles based on category and published date.  
- Allows navigation to related news details.  
- Includes a "Close details" button and supports system back navigation.

---

## 🛠 Technical Details

- **Kotlin** + **Jetpack Compose** for declarative UI.  
- **MVVM architecture** with ViewModel and Repository pattern.  
- **Retrofit** and coroutines for asynchronous web service calls.  
- **Room** for local persistence of news and tags.  
- Caching logic to reduce network calls and update UI efficiently.  
- Material 3 components such as Chips and DateRangePicker for UI consistency.

---

## 🧪 Testing & Validation

- LazyColumn used for efficient scrolling and lazy loading.  
- Filtering chips visually highlight selected category.  
- Detailed screen tests verify display of all fields and navigation flow.  
- Offline mode handled by Room persistence.  
- Duplicate prevention and featured article marking verified.

---

##📸 Screenshots




<img width="356" height="793" alt="Screenshot 2025-07-28 165517" src="https://github.com/user-attachments/assets/e811dfaf-19d9-43a0-b611-6a07d0732c15" />
<img width="361" height="797" alt="Screenshot 2025-07-28 165549" src="https://github.com/user-attachments/assets/9c40e5ea-d71e-4de1-92a6-679a04f04449" />
<img width="358" height="762" alt="Screenshot 2025-07-28 165605" src="https://github.com/user-attachments/assets/3425bca7-c74d-4aea-bc06-4da3dd05bc2d" />
<img width="354" height="791" alt="Screenshot 2025-07-28 165618" src="https://github.com/user-attachments/assets/f5e4bfb5-8af2-4628-ad4f-d69f5501efaf" />



## 📥 How to Run (Step-by-step)

1. **Clone the project**  
   ```bash
   git clone https://github.com/avelic5/NewsFeedApp.git
Open the project in Android Studio

Start Android Studio and open the cloned NewsFeedApp folder.

Build and run the app

Connect an Android device or start an emulator.

Click the Run button in Android Studio.

Enjoy the app

Browse, filter, and view news articles.

🤝 Contribution
Contributions, issues, and feature requests are welcome! Feel free to fork the repository and submit a pull request.
If you need any help running or extending the app, feel free to ask!
