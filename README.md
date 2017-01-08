# Devil Reader

![App Icon](https://raw.githubusercontent.com/jwennis/devilreader/master/app/src/main/res/mipmap-mdpi/ic_launcher.png "App Icon") 

Devil Reader is a currated news reader app and capstone project for my [Android Developer Nanodegree](https://www.udacity.com/course/android-developer-nanodegree-by-google--nd801). The app focuses on news and information regarding the [New Jersey Devils](https://www.nhl.com/devils/) of the [National Hockey League](https://www.nhl.com).

##Discover

The entry point to the app displays a summary of the last game played and preview of the next upcoming game, latest headlines, and a visualization of the teams' probability of making the playoffs.

<img src="https://raw.githubusercontent.com/jwennis/devilreader/master/screens/2017-01-08%2020.44.28.png" width="250">
<img src="https://raw.githubusercontent.com/jwennis/devilreader/master/screens/2017-01-08%2020.44.33.png" width="250">

## News

The News section lists the latest articles, showing only unread articles by default. Filters can be used to display previously read articles (which appear faded to indicate such), saved articles, and to filter which news sources to display content from.

Users can swipe to dismiss and long press to save articles. Clicking an article will display the details and contents of the article, including tags of any players mentioned in the article. Tagged players can be clicked on to see their details (more on this below). Additionally, the link to the article can be launched in a Chrome Custom Tab or the users' default web browser (as per user setting in SharedPreferences) or shared via any app on the users device capable of handling a URL.

<img src="https://raw.githubusercontent.com/jwennis/devilreader/master/screens/2017-01-08%2020.44.53.png" width="250">
<img src="https://raw.githubusercontent.com/jwennis/devilreader/master/screens/2017-01-08%2020.45.13.png" width="250">
<img src="https://raw.githubusercontent.com/jwennis/devilreader/master/screens/2017-01-08%2021.38.39.png" width="250">

## Schedule

The Schedule shows both completed and upcoming games. Clicking on a game launches the detail view, which includes stats, a short article blurb, a video recap and additionally videos for each individual goal.

<img src="https://raw.githubusercontent.com/jwennis/devilreader/master/screens/2017-01-08%2020.47.19.png" width="250">
<img src="https://raw.githubusercontent.com/jwennis/devilreader/master/screens/2017-01-08%2020.47.42.png" width="250">

<img src="https://raw.githubusercontent.com/jwennis/devilreader/master/screens/2017-01-08%2020.47.55.png" width="250">
<img src="https://raw.githubusercontent.com/jwennis/devilreader/master/screens/2017-01-08%2020.48.05.png" width="250">

## Roster

The Roster section displays players on the team, seperated by position (forward, defense, goaltender) or status (injury reserve, non-roster), as well as a salary chart which can be scrolled horizontally to view future years.

<img src="https://raw.githubusercontent.com/jwennis/devilreader/master/screens/2017-01-08%2020.45.30.png" width="500">

Clicking on a player launches the detail view which includes a list of news articles (similar to the News section above, but filters only news the given player is tagged in), contract history, and lists all of the goals the player has scored in the current season, including video clips.

<img src="https://raw.githubusercontent.com/jwennis/devilreader/master/screens/2017-01-08%2020.46.09.png" width="250">
<img src="https://raw.githubusercontent.com/jwennis/devilreader/master/screens/2017-01-08%2020.46.28.png" width="250">
<img src="https://raw.githubusercontent.com/jwennis/devilreader/master/screens/2017-01-08%2020.46.39.png" width="250">

## Authentication

Devil Reader makes use of Google Sign-in and Firebase Authentication to allow users to sync their read and saved articles between the cloud and their devices. Saving an article on one device will save it on other devices the user logs into as well, and read stories won't show up if they've previously been read on another device.

<img src="https://raw.githubusercontent.com/jwennis/devilreader/master/screens/2017-01-08%2020.44.40.png" width="250">

## Feature Roadmap

- League standings
- Prospect tracker
- Draft history
- Transaction history
- "Armchair GM": drag & drop line ups, sign and trade players
- Purchase tickets (referral programs w/ 3rd party venders)
- Player career stats
- Team stats + advanced stats/analysis/graphs
- Additional news sources
- Live game chat
- Native media player (ExoPlayer)
- Chromecast support
- Proper AndroidTV support
- AndroidTV Live Channels support
