#!/bin/bash

echo '\033[32m Checkout to v4-dev\033[0m'
git checkout v4-dev

echo '\033[32m Push to origin v4-dev\033[0m'
git push origin v4-dev
echo '\033[32m Push to osc v4-dev\033[0m'
git push osc v4-dev
