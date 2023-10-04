#!/bin/bash
ELM=$(cat pom.xml | grep "version" | cut -d'>' -f2 | cut -d'<' -f1)
a=(${ELM[@]})
version=${a[0]}

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
jar cfM "$name.jar" -C bin/ . # //TODOD  -C classPath
rm -fr bin

# scp "$name.jar" "root@147.182.209.156:/root/fourteam/nginx/html/downloads/jars/${name}/${name}-latest.jar"
# scp "$name.jar" "root@147.182.209.156:/root/fourteam/nginx/html/downloads/jars/${name}/${name}-${version}.jar"

