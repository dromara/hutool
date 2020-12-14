#!/bin/bash

exec mvn -T 1C clean source:jar javadoc:javadoc install -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Dmaven.compile.fork=true
