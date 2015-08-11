#!/bin/bash

exec mvn clean source:jar install -Dmaven.test.skip=true