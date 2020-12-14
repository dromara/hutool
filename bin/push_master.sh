#!/bin/bash

echo -e "\033[32mCheckout to v5-master\033[0m"
git checkout v5-master

echo -e "\033[32mMerge v5-dev branch\033[0m"
git merge v5-dev -m 'Prepare release'

echo -e "\033[32mPush to origin v5-master\033[0m"
git push origin v5-master
echo -e "\033[32mPush to osc v5-master\033[0m"
git push osc v5-master
