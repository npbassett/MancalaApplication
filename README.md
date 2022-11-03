# MancalaApplication
<p align="center">
  <img src="https://github.com/npbassett/MancalaApplication/blob/main/app/src/main/res/drawable/mancala_logo.png" width="800">
</p>

## About
This Android application to play the game mancala was made in Android Studio and follows an MVVM architecture pattern. The app features an AI opponent that chooses moves using a bounded minimax algorithm with alpha-beta pruning to improve efficiency. The app also implements a local database through Room to track the number of games played and the outcomes of those games.

## Features
- Multiplayer mode.
- Single player mode with AI opponent, implemented using a bounded minimax algorithm.
  - Easy: maxDepth = 1
  - Intermediate: maxDepth = 3
  - Hard: maxDepth = 8
- Keeps track of statistics including games played and win percentage.

## Screenshots
A [one minute demo](https://youtu.be/a9F-5GTPAWs) is available on Youtube.

<img src="/readme/mancala_app_homepage_screenshot.png" width="400" hspace="10" align="left">
<img src="/readme/mancala_app_gameplay_screenshot.png" width="400" hspace="10" align="left">
