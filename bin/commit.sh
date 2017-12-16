#!/bin/bash

git add .
git commit -am "$1"

bin/push_dev.sh
