#!/bin/bash

./gradlew test --tests "edu.ship.engr.discordbot.TestSuite" --console=plain || exit 1