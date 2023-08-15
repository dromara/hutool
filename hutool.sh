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
