#!/usr/bin/env bash

(cd ../ && gradle shadowJar)
cp ./../build/libs/"Ship ENGR Bot.jar" ./../stage/
java -jar "Ship ENGR Bot.jar"