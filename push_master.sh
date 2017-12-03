#!/bin/bash

echo 'checkout to master'
git checkout master

echo 'Merge dev branch'
git merge dev

echo 'Push to origin master'
git push origin master
echo 'Push to osc master'
git push osc master
