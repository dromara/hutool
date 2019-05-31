#!/bin/bash

echo -e "\033[32mCheckout to v4-master\033[0m"
git checkout v4-master

echo -e "\033[32mMerge v4-dev branch\033[0m"
git merge v4-dev -m 'Prepare release'

echo -e "\033[32mPush to origin v4-master\033[0m"
git push origin v4-master
echo -e "\033[32mPush to osc v4-master\033[0m"
git push osc v4-master
