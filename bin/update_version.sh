#!/bin/bash

#
# Copyright (c) 2024 Hutool Team and hutool.cn
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

#------------------------------------------------
# 升级Hutool版本，包括：
# 1. 升级pom.xml中的版本号
# 2. 替换README.md和docs中的版本号
#------------------------------------------------

# show Hutool logo
"$(dirname ${BASH_SOURCE[0]})"/logo.sh

if [ -z "$1" ]; then
        echo "ERROR: 新版本不存在，请指定参数1"
        exit
fi

# 替换所有模块pom.xml中的版本
mvn versions:set -DnewVersion=$1

# 不带-SNAPSHOT的版本号，用于替换其它地方
version=${1%-SNAPSHOT}

# 替换其它地方的版本
"$(dirname ${BASH_SOURCE[0]})"/replaceVersion.sh "$version"
