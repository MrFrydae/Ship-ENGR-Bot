#!/usr/bin/env bash

SCREEN=$(screen -S DiscordBot -X select .; echo $?)
function screenExists() {
    if [[ "${SCREEN}" == "0" ]]; then
        true
    else
        false
    fi
}

case "$1" in
    "j" | "join")
        if screenExists; then
            screen -r "DiscordBot"
            exit 0
        else
            echo "The bot is not running"
            exit 1
        fi
    ;;
    "start")
        if screenExists; then
            echo "The bot is already running"
            screen -r DiscordBot
            exit 1
        else
            screen -dmS DiscordBot java -jar "DiscordBot.jar"
            exit 0
        fi
    ;;
    "stop")
        if screenExists; then
            screen -S DiscordBot -X stop
            exit 0
        else
            echo "The bot is not running"
            exit 1
        fi
    ;;
    "c" | "compile")
        if screenExists; then
            echo "The bot is running"
            exit 1
        else
            (cd ../ && gradle shadowJar)
            cp "../build/DiscordBot.jar" ../stage/
            exit 0
        fi
    ;;
esac