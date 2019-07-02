#!/bin/bash

echo -e "\033[32mCheckout to v4-dev\033[0m"
git checkout v4-dev

echo -e "\033[32mPush to origin v4-dev\033[0m"
git push origin v4-dev
echo -e "\033[32mPush to osc v4-dev\033[0m"
git push osc v4-dev
