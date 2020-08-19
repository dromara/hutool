#!/bin/bash

exec mvn -T 1C clean source:jar javadoc:javadoc package -Dmaven.test.skip=false -Dmaven.javadoc.skip=false
