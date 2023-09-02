# Speed Math

An Android app that helps you improve your mental math skills in arithmetic (addition, subtraction, multiplication, and division as of v0.1.0).

## Features

- Difficulty setting: the game can be played in either easy or hard mode. The range of possible numbers you see in the questions depend on the difficulty mode (i.e. the range will be wider on hard mode)
- Text Mode: questions are displayed on the screen
- Audio Mode: questions are not displayed and instead spoken by a text-to-speech engine, and must be listened to
- Statistics pages: for the speedrunners out there, you can keep track of the history of your fastest times for each game mode and difficulty mode, so you can compete with yourself, as well as others for the fastest time. For those looking to improve their answering speeds, the app graphs your daily average answering speeds for each type of arithmetic question

## How It's Made

**Tech used:** Kotlin, Android SDK, Room Database (an abstraction of SQLite)

I started Speed Math as a project to learn Android development in 2021. The initial version that was finished by the end of that summer only had Text Mode, no statistics pages, and the player's fastest times were saved in a plain text file as a flat database.

I was inspired to return to developing Speed Math in 2023 when I encountered situations where numbers come up in verbal conversation (e.g. discussing prices and how they add up together), which made me realize that doing mental math can be more difficult when you can't see the numbers themselves. This led to the creation of Audio Mode, and because the app would need a more robust system to save fastest times, this also lead to the implementation of the Room database, and the statstics pages.
