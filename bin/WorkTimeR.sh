#!/bin/bash
cd $(dirname $(dirname $0))
#./gradlew run --quiet --args="$1 $2 $3 $4 $5 $6 $7 $8 $9" 2>/dev/null
java -jar build/libs/WorkTimeR-1.0-SNAPSHOT-standalone.jar $1 $2 $3 $4 $5 $6 $7 $8 $9 2>/dev/null

