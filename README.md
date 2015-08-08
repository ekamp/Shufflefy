# Shufflefy
Short and sweet android application that instantly plays a user's Spotify library. Simple UI implementation designed to mock the current ViewPager implementation seen in Spotify's Android application. (Min SDK 14)

![Alt text](https://github.com/ekamp/Shufflefy/blob/master/sampleScreen.png "Application Screenshot")

##Architecture
The main structure of this application revolves around the Model View Controller (MVC) architecture. The application utilizes Retrofit coupled with Otto and a centralized controller instance. This architecture allows the view (Activity), to request for track information from the controller (semi-content provider), which interfaces with Retrofit in order to request and parse infomation, passing such information back to the View through Otto's event bus callback subscription(mimicking a Local Broadcast Receiver) present in the Acitivity instance.

|View| <--> |Controller| <--> |Model|

##Libaries Utilized 
- Square's Retrofit for content services.
- Square's Otto for an application-wide event bus.
- Square's Picasso for smooth image loading.
- Google's Support Percent Library for View Scaling.

##Known Issues
- ViewPager does not scroll to next Fragment once a track has completed playing.
- SpotifyPlayer instance does not start playing a track when .skipToPrevious() is called on the queue.
