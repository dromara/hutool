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

# Help info function
help(){
  echo "--------------------------------------------------------------------------"
  echo ""
  echo "usage: ./hutool.sh [install | doc | pack]"
  echo ""
  echo "-install    Install Hutool to your local Maven repository."
  echo "-doc        Generate Java doc api for Hutool, you can see it in target dir"
  echo "-pack       Make jar package by Maven"
  echo ""
  echo "--------------------------------------------------------------------------"
}


# Start
./bin/logo.sh
case "$1" in
  'install')
    bin/install.sh
	;;
  'doc')
    bin/javadoc.sh
	;;
  'pack')
    bin/package.sh
	;;
  *)
    help
esac
