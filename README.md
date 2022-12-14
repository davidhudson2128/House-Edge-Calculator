# House-Edge-Calculator

This Android app is published on the Google Play Store, check it out [here](https://play.google.com/store/apps/details?id=com.odds.bet). To view my other apps, click [here](https://play.google.com/store/apps/developer?id=David+Hudson+Apps).

The house edge calculator can be used to calculate the percentage cut, or house edge, the sportsbook is taking based on the odds provided. The calculator also calculates the probability of each outcome occurring based on the odds given by the sportsbook. Odds must be in American format.

Built using Kotlin and XML in Android Studio.


Demo:

https://user-images.githubusercontent.com/71290098/207667790-8c724302-85f4-462f-abcd-448fcf9ea248.mp4

Examples:

![house-edge-demo](https://user-images.githubusercontent.com/71290098/207718410-8ced4a11-467d-4ac8-8a63-6f73901aeabd.png)

In the example above, the sportsbook is giving the NY Knicks +150 odds to win, which has a 38.6% chance. The sportsbook is giving the CHI Bulls -175 odds to win, which has a 61.4% chance. The calculated house edge which is built-in to these odds is 3.51%.

Assuming the sportsbook's estimation for the odds is accurate, this means that for every $1 bet on this game, on either team, the sportsbook is taking on average a $0.0351 cut.


![house-edge-demo-2](https://user-images.githubusercontent.com/71290098/207719459-a87b46a7-43df-4b4a-ba3a-4d3d84a27ee0.png)

In this example, the soccer match has three possible outcomes, a draw or either team winning. The sportsbook is giving Brentford +240 odds, a draw +255 odds, and Tottenham +105 odds. The implied probability for each of these outcomes is 27.65%, 26.48%, and 45.86% respectively. The calculated house edge built-in to these odds is 5.98%.

Assuming the sportsbook's estimation for the odds is accurate, this means that for every $1 bet on this game, on either team, the sportsbook is taking on average a $0.0598 cut.
