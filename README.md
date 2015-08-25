# Shufflefy
Short and sweet android application that instantly plays a user's Spotify library. Simple UI implementation designed to mock the current ViewPager implementation seen in Spotify's Android application. (Min SDK 14)

![Alt text](https://github.com/ekamp/Shufflefy/blob/master/sampleScreen.png "Application Screenshot")

##Architecture
The main structure of this application revolves around the Model View Controller (MVC) architecture. The application utilizes Retrofit coupled with Otto and a centralized controller instance. This architecture allows the view (Activity), to request for track information from the controller (semi-content provider), which interfaces with Retrofit in order to request and parse infomation, passing such information back to the View through Otto's event bus callback subscription(mimicking a Local Broadcast Receiver) present in the Acitivity instance.

![Alt text](https://github.com/ekamp/Shufflefy/blob/master/UMLDesign.png "Application Architecture")

## Future Features
- Drawer to display user account information.
- Drawer element to allow a user to change the queue to a Playlist.

##Libaries Utilized 
- Square's Retrofit for content services.
- Square's Otto for an application-wide event bus.
- Square's Picasso for smooth image loading.
- Square's ButterKnife for view injection, allows for cleaner view based code.
- Google's Support Percent Library for View Scaling.
- Spotify's Authentication library 1.0 beta 10 (Used for authenticating a user)
- Spotify's Player library 1.0 beta 10 (Used to manage a user's track queue and track play state)

##Known Issues
- SpotifyPlayer instance does not start playing a track when .skipToPrevious() is called on the queue.
