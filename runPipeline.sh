#!/bin/bash

./gradlew :Commands:checkstyleMain :Commands:checkstyleTest --console=plain || exit 1

./gradlew test --tests "edu.ship.engr.discordbot.TestSuite" -x :Commands:test --console=plain || exit 1