#!/bin/bash

mvn clean deploy -P release -Dgpg.passphrase=$1