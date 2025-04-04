### CS2212 Group 57 Project

This software is a pet game that allows you to take care of a pet through
various actions with an extra feature of having events that reward the user
with food and gifts they can use for their pet.

**Required Libraries:**
    - Windows JavaFX SDK version 24 which can be found at https://gluonhq.com/products/javafx/  
    - JSON for Java which can be downloaded here: https://repo1.maven.org/maven2/org/json/json/20250107/  
    - JUnit (for testing) which can be imported as a library in Intellij by clicking file -> project structure  
        -> libraries -> the "+" sign -> from maven and then searching for  
        "com.github.cschabl.cdi-unit-junit5:cdi-unit-junit5:0.4" (remove comments)
    
**How to build:**
    - Download JavaFX (as explained above)  
    - In the top right click Run config -> modify options -> add VM options  
    - In VM options, add: "--module-path "<path/to/JavaFX/lib(include \lib)>"  
        --add-modules javafx.controls,javafx.fxml,javafx.media  --add-opens javafx.base/com.sun.javafx=ALL-UNNAMED"  
        (remove outer comments)
    - In project structure, click libraries -> "+" and add the path to your JavaFX lib folder  
    - Then you can Run the project  

**Executable:**
Prerequisites
    - As outlined previously, you need JavaFX and org.json in order to run the application (not including tests). You will also need the group57.jar file that is provided in the main repository.
Executing
    - In order to run the jar file, you must enter this command in the command-line interface in the folder with the .jar file:
    - java --module-path **<path-to-jaxafx-lib-folder-on-your-machine>** --add-modules javafx.controls,javafx.fxml,javafx.media  --add-opens javafx.base/com.sun.javafx=ALL-UNNAMED -jar group57.jar
    - Note that for the module-path argument, you must place a string ("") with the path to the javafx lib folder on your machine.


**User Guide:**
Objective:  
To take care of a pet and keep it happy, healthy, and alive for as long as possible. Throughout the game, you must  
monitor your pet’s vital stats: health, sleep, fullness, and happiness. These stats decrease over time, and it's  
your job to keep them up by feeding your pet, letting it sleep, playing with it, giving gifts, or taking it to  
the vet when needed.

How to start game:
1. Press "new game"
2. select animal type and type name
3. Save and create new game. This will return you to the main menu.
4. Use drop down to select new game that you created
5. Press Load Game 

How to play game:  
There are 5 main action buttons once you start the game: (F) feed pet, (S) put the pet to sleep,  
(V) take to vet, (G) give gift, (P) play with pet. You can use either the keys or the buttons on  
screen to trigger actions By using these actions, you will help the pet stay alive and increase your XP.  

Event button (T):  
The event button generates a new screen that challenges you to make decisions about an event. If you select the  
right option from the list, you will be rewarded with a gift. If you select the wrong option, you will be  
punished a certain number of XP or have one of you vital stats decreased.

**Password**  
Parental Controls: group57


