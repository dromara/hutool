#!/bin/bash

echo -e "\033[32mCheckout to v6-dev\033[0m"
git checkout v6-dev

echo -e "\033[32mPush to origin v6-dev\033[0m"
git push origin v6-dev
echo -e "\033[32mPush to osc v6-dev\033[0m"
git push osc v6-dev
