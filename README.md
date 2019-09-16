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
    
## Recompiling and Restaring the bot code
* Run the following command:
    * ``./bot update``
