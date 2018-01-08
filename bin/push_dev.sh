#!/bin/bash

echo 'checkout to v4-dev'
git checkout v4-dev

echo 'Push to origin v4-dev'
git push origin v4-dev
echo 'Push to osc v4-dev'
git push osc v4-dev
