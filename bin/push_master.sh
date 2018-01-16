#!/bin/bash

echo '\033[32m checkout to v4-master\033[0m'
git checkout v4-master

echo '\033[32m Merge v4-dev branch\033[0m'
git merger v4-dev -m 'Prepare release'

echo '\033[32m Push to origin v4-master\033[0m'
git push origin v4-master
echo '\033[32m Push to osc v4-master\033[0m'
git push osc v4-master
