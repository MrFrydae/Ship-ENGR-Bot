#!/bin/bash

./gradlew checkstyleMain -x :Commands:checkstyleMain || exit 1
./gradlew checkstyleTest -x :Commands:checkstyleTest || exit 1
./gradlew :Commands:checkstyleMain || exit 1
./gradlew :Commands:checkstyleTest || exit 1
./gradlew build -x test -x checkstyleMain -x checkstyleTest -x :Commands:test -x :Commands:checkstyleMain -x :Commands:checkstyleTest || exit 1
./gradlew :Commands:build -x :Commands:test -x :Commands:checkstyleMain -x :Commands:checkstyleTest || exit 1
./gradlew :Commands:test || exit 1