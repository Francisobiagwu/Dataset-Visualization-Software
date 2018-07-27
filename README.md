Dataset Visualization Software
==============================


### Introduction ###

   Dataset Visualization Software is a SE-577 group project at Drexel University. The primary goal of this project is to design and apply software design patterns, and attributes to a website. 

#### Patterns applied ####

    Visitor pattern
    Builder pattern
    Strategy pattern
    Singleton pattern
    Decorator pattern
    Observer pattern
    Template pattern
    Iterator pattern



#### Attributes applied ####

    Security attributes
    Usability attributes
    Strategy attributes
    Maintanability attributes
    Portability attributes
    Interoperability attributes
    

## Building with Gradle

This comes with the gradle wrapper, so all one needs to do to run the build
is `./gradlew build` or `gradlew.bat build` on windows (needs verification).

-----------------------------------------
Running the software
-----------------------------------------

## Run with Gradle
To run the software and host the website `./gradlew run` or `gradlew.bat run`.

## Access the web site
Browse to the application website at http://localhost:4567/

The website provides links to each of our UCs within the left navigation bar.  

Use case features include:
* Upload a Dataset : Select a file to upload as a dataset. The file must conform to specific format. Click link for file format details.
* Define a Dataset : Name then describe a new dataset. Upload the dataset to the server.
* Append To Existing Dataset : User selects an existing dataset using the pages UI to add new samples. 
* Visualize Dataset : User selects an existing dataset and visualization method. Data displayed using the chosen visualization method.


The website is responsive. When the browsers screen bounds are narrow navigation options will apear within the top left navigation pull down as seen under "Responsive Website Layout" below.
