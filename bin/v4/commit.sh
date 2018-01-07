#!/bin/bash

git add .
git commit -am "$1"

bin/v4/push_dev.sh
