# Ship Engineering Bot
This is the "Official" Discord bot the the Shippensburg University's Engineering Department

## Setting up the bot
#### Gradle 5.5.x is required for compilation
* Step 1: Create a folder with any name you want.
* Step 2: ``cd`` into that folder and create a folder named ``stage``
* Step 3: ``cd`` into that folder and create a file named ``bot.sh``
* Step 4: copy the included code into ``bot.sh``
* Step 5: run the following commands:
    * ``chmod -x bot.sh``
    * ``./bot.sh clone``
* Step 6: Find the file named ``config.json.sample`` in the ``resources`` directory
* Step 7: Make a copy of that file and name it ``config.json``
* Step 8: Input the values that you have been provided
* Step 9: Add the following files to ``stage``
    * ``crews.csv``
    * ``offerings.csv``
    * ``professors.csv``
    * ``students.csv``
    * ``users.csv``
* Step 10: Run the following commands:
    * ``./bot.sh compile``
    * ``./bot.sh start``
    
## Stopping the bot
* Run the following command:
    * ``./bot.sh stop``
* If that doesn't work, have a discord admin run ``!stop`` in chat
    
## Updating the bot code
* Run the following command:
    * ``./bot.sh update``
    

#### bot.sh
```bash
#!/usr/bin/env bash

SCREEN=$(screen -ls | grep -q "....\.DiscordBot"; echo $?)
function screenExists() {
    if [[ "${SCREEN}" == "0" ]]; then
        true
    else
        false
    fi
}

function clone() {
    cd ../
    git init && git remote add origin http://github.com/MrFrydae/Ship-ENGR-Bot.git
    git pull origin master
    cd stage
}

function compile() {
    (cd ../ && gradle shadowJar)
    cp "../build/DiscordBot.jar" ../stage/
}

function pull() {
    cd ../
    git pull origin master
    cd stage
}

function start() {
    screen -dmS DiscordBot java -jar "DiscordBot.jar"
}

function stop() {
    screen -S DiscordBot -X stop
}

function join() {
    screen -r "DiscordBot"
}

case "$1" in
    "clone")
        clone
        exit 0
    ;;
    "join")
        if screenExists; then
            join
            exit 0
        else
            echo "The bot is not running"
            exit 1
        fi
    ;;
    "start")
        if screenExists; then
            echo "The bot is already running"
            join
            exit 1
        else
            start
            exit 0
        fi
    ;;
    "stop")
        if screenExists; then
            stop
            exit 0
        else
            echo "The bot is not running"
            exit 1
        fi
    ;;
    "compile")
        if screenExists; then
            stop && compile && start
            exit 1
        else
            compile
            exit 0
        fi
    ;;
    "update")
        if screenExists; then
            stop
        fi
        
        pull && compile && start
    ;;
esac
```