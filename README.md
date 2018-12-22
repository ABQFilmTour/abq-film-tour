# ABQ Film Tour

### Aims
ABQ Film Tour is an Android app for tourists and film enthusiasts in the Albuquerque area. Users can find, submit, and discuss locations featured in locally filmed movies and television series such as "Breaking Bad" and "Better Call Saul".

### Functional inventory
https://github.com/ABQFilmTour/ABQFilmTour
This is the repository for the client Android application that the end user will use.

https://github.com/ABQFilmTour/ABQFilmTourBackend
This is the repository for the server side backend REST API. It is a Spring Boot project using an Apache Derby database.

### Team
[**Joseph Scott Peterson**](https://jscpeterson.github.io)
Backend engineer, set up and maintained the server, set up backend to frontend communication, activity stack management and shared preferences.

**Samuel Andazola**
Frontend developer, design manager, created custom logo, splash page, map pins and app theme, in charge of layout design, worked on controllers and repositories in backend.

**Rebecca Farish**
Frontend developer, mathematics consultant, responsible for Google authentication sign-in, device permissions, device location, and layout design, worked on controllers and repositories in backend.

### Web service
The REST API is hosted at jscpeterson.com. <br />
jscpeterson.com/rest/users/ <br />
jscpeterson.com/rest/film_locations/ <br />
jscpeterson.com/rest/film_locations/{location_id}/user_comments/ <br />
jscpeterson.com/rest/film_locations/{location_id}/images/ <br />

### State of completion
The client authenticates the user with oauth2 authentication but we are still working on verification on the server side. We will also need to implement admin requests on the server so we can manipulate the data for moderation and adding new content. From the client the user can access the dataset but cannot post new content. The user can explore the map, filter locations by a production, and pull up location specific data. On the location screen we display text content specific to the location but are only displaying static drawables. The ability to post and retrieve images is the third major hurdle after security and being able to post submissions and comments. Other minor issues - submission should be disabled when no title is selected or otherwise handled. We are unaware of any severe bugs. 
Highest priority hitlist:
- ~Oauth2 verification on the server~
- ~Allow for admin requests to the server~
- Location submissions from the client
- Comment submissions from the client
- Image submission/retrieval

### Test platforms
The minimum required Android API is 21. It has been successfully tested on emulators and phones running API 26, 27 and 28. Currently the app only runs in English and portrait orientation.

### Third-party libraries
- Google Maps
- Google Authentication
- Retrofit
- Apache CSV
- Circle Image View
- [Glide](http://bumptech.github.io/glide/)

### External services
-  Google Sign In
-  Google Maps
-  [OMDb API](http://www.omdbapi.com/)
-  [City of Albuquerque public film locations](http://data.cabq.gov/business/filmlocations/filmlocationsJSON_ALL) Retrieved on 12/3/2018

### Aesthetic goals
- Making user comment box more user friendly, display more information
- ~Pull in IMDB poster for location activity.~

### Stretch goals
- Handle picking from multiple images on the UI
- Request throttling
- Using room to cache data so the app is still usable offline, 
- Save bookmarked locations to shared preferences
- A ‘create your own tour’ feature.

### Relevant Links

[Wireframes](https://xd.adobe.com/view/81f12600-75b1-4f07-7cc3-a86bc45dacf9-f7c0/)

[User Stories](docs/UserStories.md)

[ERD](docs/ERD.pdf)

[DDL](https://abqfilmtour.github.io/ABQFilmTourBackend/create.sql)

[Javadocs for the frontend](docs/api/index.html) <br />
[Javadocs for the backend](https://abqfilmtour.github.io/ABQFilmTourBackend/docs/api/index.html)

[REST documentation](https://abqfilmtour.github.io/ABQFilmTourBackend/docs/rest/api.html)

[Licenses](docs/Licenses.md)

[Build Instructions](docs/BuildInstructions.md)

[Usage Instructions](docs/UsageInstructions.md)

<sub>This site provides applications using data that has been modified for use from its original source, www.cabq.gov, the official website of the City of Albuquerque. The City of Albuquerque makes no claims as to the content, accuracy, timeliness, or completeness of any of the data provided at this site. The data provided at this site is subject to change at any time. It is understood that the data provided at this site is being used at one’s own risk.</sub>
