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

# show Hutool logo
"$(dirname ${BASH_SOURCE[0]})"/logo.sh

# 保证当前在v6-dev分支
git checkout v6-dev

# 同时同步Gitee、Github和Gitcode的库
git pull osc v6-dev
git pull origin v6-dev
git pull gitcode v6-dev
