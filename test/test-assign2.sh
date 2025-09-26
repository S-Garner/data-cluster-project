#!/usr/bin/env bash
set -euo pipefail

# go to project root (one level above this script)
cd "$(dirname "$0")/.."

# ensure build exists
if [[ ! -d out ]]; then
  echo "No 'out' directory. Build first: javac -d out src/*.java"
  exit 1
fi

# dataset -> K mapping from the assignment
declare -A KMAP=(
  [ecoli]=8
  [glass]=6
  [ionosphere]=2
  [iris_bezdek]=3
  [landsat]=6
  [letter_recognition]=26
  [segmentation]=7
  [vehicle]=4
  [wine]=3
  [yeast]=10
)

for name in ecoli glass ionosphere iris_bezdek landsat letter_recognition segmentation vehicle wine yeast; do
  file="data/${name}.txt"
  if [[ ! -f "$file" ]]; then
    echo "Missing file: $file"
    continue
  fi
  java -cp out Main "$file" "${KMAP[$name]}" 100 0.001 100
done
