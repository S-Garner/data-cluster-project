#!/bin/bash

javac -d out src/*.java

for filename in ./data/*.txt; do
    [ -e "$filename" ] || continue
    java -cp out Main "$filename" 15 100 0.000001 100
done
