#!/bin/bash

echo -e "\033[32m Checkout to master\033[0m"
git checkout master

echo -e "\033[32m Merge dev branch\033[0m"
git merge dev

echo -e "\033[32m Push to origin master\033[0m"
git push origin master
echo -e "\033[32m Push to osc master\033[0m"
git push osc master
