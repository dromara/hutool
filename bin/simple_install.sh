#!/bin/bash

exec mvn -T 1C clean install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true
