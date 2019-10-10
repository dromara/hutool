#!/bin/bash

echo -e "\033[32mCheckout to v5-dev\033[0m"
git checkout v5-dev

echo -e "\033[32mPush to origin v5-dev\033[0m"
git push origin v5-dev
echo -e "\033[32mPush to osc v5-dev\033[0m"
git push osc v5-dev
