#!/usr/bin/env bash

(cd ../ && gradle shadowJar)
cp "../build/DiscordBot.jar" ../stage/
java -jar "DiscordBot.jar"