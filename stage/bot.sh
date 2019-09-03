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
    "j" | "join")
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
esac