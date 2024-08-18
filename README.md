# FindGame

FindGame is an app that allows you to check which games you have on Steam and find new players with a similar game library. Additionally, our app can suggest what you might want to play at any given moment.

[FindGame Trello Board](https://trello.com/b/VOrvaDpT/findapp)

### DEMO ACCOUNT

This demo account has a connected Steam profile:
[Staging Server](https://findgame-341ba.web.app/)
- **Login:** demo@gmail.com
- **Password:** demo

## Features

- **Add Friends:** Easily add new friends to your list based on shared games and interests.
- **Discover New Players:** Find and connect with new players who have similar game libraries, enhancing your multiplayer experience.
- **Game Roulette:** Can't decide what to play? Use the "Draw a Game" feature to randomly select a game from your shared library.
- **Browse Steam Library:** View and explore your entire Steam games library directly within the app.
- **Profile Update:** The app automatically updates your profile every 15 minutes to ensure your information is always current.
- **Daily Library Update:** Your game library is refreshed daily at 11:00 PM to include any new additions or changes.


### Simple Authentication

Easily log in using your Steam credentials to access your personalized game library and social features.

![Simple auth](https://github.com/user-attachments/assets/dae6200c-ec18-4587-b449-c810b6d4642c)

### Games Library

View your entire Steam games library with detailed information on each game.

![Games library](https://github.com/user-attachments/assets/2388f9f0-16fc-4775-bf2a-102dc6ddac09)

### Discover Users

Find new friends who have the same games! Thanks to the Similarity Score, you'll always know how closely a user matches your interests.

![Discovery user](https://github.com/user-attachments/assets/170ea400-52c9-4773-85d1-0623685a7485)

### Game Recommendations

Get personalized game suggestions based on your current mood and preferences, helping you find the perfect game to play.

### Find a Game to Play with Friends by Drawing Lots

Can’t decide what to play? Use the drawing lots feature to randomly select a game from your shared library.

![Drawing lots](https://github.com/user-attachments/assets/403f1d27-af90-4ae5-ba65-19933d35690b)
![Drawing lots](https://github.com/user-attachments/assets/4526c78f-8e86-40df-95e6-6bce279751dd)
![Drawing lots](https://github.com/user-attachments/assets/dcaf7395-2bf7-43d7-80ad-14bf39870f97)
![Drawing lots](https://github.com/user-attachments/assets/4bb2192e-d94e-4dc8-b043-78028827a4cb)

## Technologies Used

- **React:** A JavaScript library for building user interfaces.
- **Java:** The primary programming language used for the backend, handling core business logic and API development.
- **Spring Boot:** A Java-based framework used to create standalone, production-grade Spring-based applications.
- **Redis:** An in-memory data structure store, used as a database, cache, and message broker to handle real-time data and improve performance.
- **MariaDB:** A relational database management system used for storing user data and game libraries.
- **i18n:** Internationalization support to provide multi-language functionality, making the app accessible to users worldwide.
- **Steam Web API:** Integration with Steam to fetch user game data.
- **Socket.io:** Real-time communication between clients and the server for live user discovery.
- **Tailwind CSS:** Utility-first CSS framework for styling the user interface.
- **Docker:** Containerization of the application for easier deployment and scalability.

