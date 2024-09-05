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

#-----------------------------------------------------------
# 此脚本用于每次升级Hutool时替换相应位置的版本号
#-----------------------------------------------------------

set -o errexit

pwd=$(pwd)

echo "当前路径：${pwd}"

if [ -n "$1" ];then
    new_version="$1"
    old_version=$(cat "${pwd}"/bin/version.txt)
    echo "$old_version 替换为新版本 $new_version"
else
    # 参数错误，退出
    echo "ERROR: 请指定新版本！"
    exit
fi

if [ -z "$old_version" ]; then
    echo "ERROR: 旧版本不存在，请确认bin/version.txt中信息正确"
    exit
fi

# 替换README.md中的版本
sed -i "s/${old_version}/${new_version}/g" "$pwd"/README.md
sed -i "s/${old_version}/${new_version}/g" "$pwd"/README-EN.md
# 替换docs/js/version.js中的版本
sed -i "s/${old_version}/${new_version}/g" "$pwd"/docs/js/version.js

# 保留新版本号
echo "$new_version" > "$pwd"/bin/version.txt
