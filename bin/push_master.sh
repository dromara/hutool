#!/bin/bash

echo -e "\033[32mCheckout to v6-master\033[0m"
git checkout v6-master

echo -e "\033[32mMerge v6-dev branch\033[0m"
git merge v6-dev -m 'Prepare release'

echo -e "\033[32mPush to origin v6-master\033[0m"
git push origin v6-master
echo -e "\033[32mPush to osc v6-master\033[0m"
git push osc v6-master
