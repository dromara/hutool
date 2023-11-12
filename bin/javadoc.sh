#!/bin/bash

#exec mvn javadoc:javadoc

# 多模块聚合文档，生成在target/site/apidocs
exec mvn javadoc:aggregate
