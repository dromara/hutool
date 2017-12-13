#!/bin/bash

git add .
git commit -am "$1"

./push_dev.sh
