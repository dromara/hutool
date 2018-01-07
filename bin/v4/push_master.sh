#!/bin/bash

echo 'checkout to v4-master'
git checkout v4-master

echo 'Push to origin v4-master'
git push origin v4-master
echo 'Push to osc v4-master'
git push osc v4-master
