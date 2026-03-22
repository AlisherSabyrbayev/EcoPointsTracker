# EcoPointsTracker

A recycling tracking app built for **Enactus IITU** to help our city motivate households to recycle more.

The idea is simple — every time a household recycles plastic, paper, glass, or metal, they earn **eco points** (10 points per kg). These points will eventually be redeemable for rewards. This is the first version: a console-based tracker that handles registration, event logging, and basic reporting.

## How it works

1. Register households with a unique ID, name, and address
2. Log recycling events — pick a material, enter the weight, done
3. View stats: recycling history, total weight, points earned
4. Generate reports: who's recycling the most, how much the community has recycled overall
5. Everything gets saved to files so data sticks around between sessions

## Running it

Open the project in IntelliJ and run `Main.java`, or from the terminal:

```
javac -d target\classes -sourcepath src\main\java src\main\java\kz\iitu\Main.java
java -cp target\classes kz.iitu.Main
```

Requires **Java 21+**.

## Project structure

```
src/main/java/kz/iitu/
├── Main.java                  — menu and user interaction
├── model/
│   ├── Household.java         — household data (id, name, address, points)
│   └── RecyclingEvent.java    — single recycling event (material, weight, date)
└── service/
    ├── DataManager.java       — manages all the data and business logic
    └── FileManager.java       — saves/loads data to text files
```

## What's next

This is v1. Future iterations will add a proper database, a web/mobile interface, and the rewards system. Stay tuned.

---

Built with Java by the Enactus IITU team.
