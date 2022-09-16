#!/bin/bash
name=${PWD##*/}
mvn clean compile assembly:single
rm -fr ./bin
mkdir -pv ./bin
arr_jar=$(find . -name '*.jar')
cd bin
for obj in $arr_jar; do
    $(jar xf ../$obj)
done
cd ../
jar cfM "$name.jar" -C bin/ .  # //TODOD  -C classPath
rm -fr bin

