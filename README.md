# Ship Engineering Bot
This is the "Official" Discord bot the the Shippensburg University's Engineering Department

## Setting up the bot
#### Gradle 5.5.x is required for compilation
* Step 1: Run the following command where you want the code to be
    * ``git clone git@gitlab.engr.ship.edu:Merlin/Ship-ENGR-Bot``
* Step 2: run the following commands:
    * ``make bot``
    * ``./bot clone``
* Step 3: Find the file named ``config.json.sample`` in the ``resources`` directory
* Step 4: Make a copy of that file and name it ``config.json``
* Step 5: Input the values that you have been provided
* Step 6: Add the following files to ``stage``
    * ``crews.csv``
    * ``offerings.csv``
    * ``professors.csv``
    * ``students.csv``
    * ``users.csv``
* Step 7: Run the following commands:
    * ``./bot compile``
    * ``./bot start``
    
## Stopping the bot
* Run the following command:
    * ``./bot stop``
* If that doesn't work, have a discord admin run ``!stop`` in chat
    
## Recompiling and Restarting the bot code
* Run the following command:
    * ``./bot update``

## Preparing for next semester
1) Update the following files with the required headers:
    1) students.csv
        * PREF_LAST_NAME
        * PREF_FIRST_NAME
        * EMAIL
        * COURSE_IDENTIFICATION
        * ACADEMIC_PERIOD
        * MAJOR_DESC
    2) offerings.csv
        * Code
        * Title
        * Frequency
        * 20##20/60
2) Run the following commands in a guild channel
    1) **!purgecoursechannels**
    2) **!enrolleveryone**