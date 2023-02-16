Mobile Apps Developer Test

General Guidelines

The app should follow Clean Architecture principles
The app should follow SOLID principles
The app should follow an architecture pattern relevant to the framework
The app should use popular libraries

The app should be responsive across different devices
The app should follow established UI/UX guidelines
The app should follow platform guidelines and best practices

The app should use Git
The app should be available on Github
Project Guidelines
Using the Open Weather API, create the following application:
Initial Screen/Dashboard screen

UI:
Displays current weather information for last searched for location
A navigation element pointing to Forecast screen
A navigation element pointing to Current Weather screen

Forecast screen

API:
Call 5 day / 3 hour forecast data

UX:
When navigating from Dashboard screen, the last searched for location should  be used to automatically load forecast

UI:
Ability to search for a location by:
City Name
Zip Code
Latitude/Longitude
Current Location
Ability to choose from last 5 locations searched
Ability to see forecast for searched location
Ability to navigate to Dashboard screen

Bonus:
Ability to filter forecast to show next 24-hours and next 48-hours
Current Weather screen

API:
Current weather data

UI:
Ability to search for a location by:
City Name
Zip Code
Latitude/Longitude
Current Location
Ability to choose from last 10 locations searched
Ability to see current weather information for searched location
Ability to switch between celsius and fahrenheit
Ability to navigate to Forecast screen
Must search for a location before being able to navigate
When navigating to Forecast screen from Current Weather screen, forecast of Current Weather screen location should automatically load
Ability to navigate to Dashboard screen

Bonus:
Persist current weather data for the last 5 locations searched
Persisted current weather data has a lifespan of 5 minutes
Use persisted current weather data for duration of lifespan	


