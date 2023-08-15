#!/bin/bash

#
# Copyright (c) 2023 looly(loolly@aliyun.com)
# Hutool is licensed under Mulan PSL v2.
# You can use this software according to the terms and conditions of the Mulan PSL v2.
# You may obtain a copy of Mulan PSL v2 at:
#          https://license.coscl.org.cn/MulanPSL2
# THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
# EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
# MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
# See the Mulan PSL v2 for more details.
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
