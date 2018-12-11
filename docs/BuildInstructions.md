## Build instructions

### 1. Clone/download the repository

From github click the "clone or download button" and copy the URL.

### 2. Import the project into Intellij IDEA

under the VCS dropdown menu select "checkout from version control" and select "git". Paste the copied URL into the URL box.
Select a directory to save the project and hit the "clone" button.

 Then from the IntelliJ IDEA main menu select "Import Project".
 
Select ABQFilmTour from the directory it was saved in. Make sure that Gradle is selected or the project will not build properly.
Click the next button. On the next menu screen check the box "Use auto-import" and then click the finish button.

### 3. Synchronize the Gradle build

Intellij IDEA should do this automatically. If not in build.gradle(Module:app) file there should be an option to do so.

### 4. Execute the build

Under the Build drop down menu select "build".  

The app should now be ready to run. Selected the device you want to install it on and hit the okay button.



